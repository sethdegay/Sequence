package dev.sethdegay.routines.feature.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.routines.core.data.repository.CalendarEventRepository
import dev.sethdegay.routines.core.data.repository.RoutineRepository
import dev.sethdegay.routines.core.designsystem.component.ProgressIndicatorAmplitudeLevel
import dev.sethdegay.routines.core.designsystem.component.TimerControlsActions
import dev.sethdegay.routines.core.model.CalendarEvent
import dev.sethdegay.routines.core.model.Routine
import dev.sethdegay.routines.core.model.Task
import dev.sethdegay.routines.core.timer.SequentialTimer
import dev.sethdegay.routines.core.timer.SequentialTimerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@HiltViewModel(assistedFactory = TimerViewModel.Factory::class)
class TimerViewModel @AssistedInject constructor(
    @Assisted private val id: String,
    private val timer: SequentialTimer<Task>,
    private val routineRepository: RoutineRepository,
    private val calendarEventRepository: CalendarEventRepository,
) : ViewModel(), TimerControlsActions {

    @AssistedFactory
    interface Factory {
        fun create(id: String): TimerViewModel
    }

    private lateinit var routine: Routine
    private lateinit var start: Instant
    private lateinit var saveCalendarEventJob: Job

    private val _uiState = MutableStateFlow<TimerUiState>(TimerUiState.Loading)
    val uiState: StateFlow<TimerUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            timer.state.collectLatest { state ->
                _uiState.update { state.asTimerUiState() }
            }
        }
        viewModelScope.launch {
            timer.start(routineRepository.getRoutine(id).also { routine = it }.tasks)
            start = Clock.System.now()
        }
    }

    override fun onToggleTimer() {
        val currentState = timer.state.value
        if (currentState is SequentialTimerState.Running<*>) {
            timer.pause()
        } else if (currentState is SequentialTimerState.Paused<*>) {
            timer.resume()
        }
    }

    override fun onPrevious() = timer.movePrevious()
    override fun onNext() = timer.moveNext()

    private fun SequentialTimerState.asTimerUiState(): TimerUiState {
        if (this is SequentialTimerState.Idle || this is SequentialTimerState.Error) {
            return TimerUiState.Loading
        }
        if (this is SequentialTimerState.Finished) {
            saveCalendarEvent()
            return TimerUiState.Loading
        }

        val (tasks, index, time, accumulatedDuration) =
            @Suppress("UNCHECKED_CAST")
            when (this) {
                is SequentialTimerState.Running<*> -> SequentialTimerStateData(
                    items as List<Task>,
                    currentItemIndex,
                    timeLeft,
                    accumulatedDuration,
                )

                is SequentialTimerState.Paused<*> -> SequentialTimerStateData(
                    items as List<Task>,
                    currentItemIndex,
                    timeLeft,
                    accumulatedDuration,
                )
            }

        val currentTask = tasks[index]

        val progress = if (routine.totalDuration > Duration.ZERO) {
            (accumulatedDuration / routine.totalDuration).toFloat()
        } else {
            1.0f
        }

        val isTimerRunning = this is SequentialTimerState.Running<*>

        return TimerUiState.Success(
            currentTask = currentTask,
            remainingTime = time,
            isTimerRunning = isTimerRunning,
            canMovePrevious = index > 0,
            canMoveNext = index < tasks.lastIndex,
            progress = progress,
            amplitudeLevel = if (isTimerRunning) {
                ProgressIndicatorAmplitudeLevel.MAXIMUM
            } else {
                ProgressIndicatorAmplitudeLevel.FLAT
            },
        )
    }

    private fun saveCalendarEvent(timeout: Duration = 10.seconds) {
        if (::saveCalendarEventJob.isInitialized && saveCalendarEventJob.isActive) {
            return
        }
        val now = Clock.System.now()
        val calendarEvent = CalendarEvent(
            start = start,
            end = now,
            duration = now - start,
            routine = routine,
        )
        saveCalendarEventJob = viewModelScope.launch {
            try {
                withTimeout(timeout) {
                    calendarEventRepository.insertCalendarEvent(calendarEvent)
                }
            } catch (_: TimeoutCancellationException) {
                // TODO handle error
            } finally {
                _uiState.update { TimerUiState.Finished }
            }
        }
    }

    override fun onCleared() {
        if (::saveCalendarEventJob.isInitialized) {
            saveCalendarEventJob.cancel()
        }
        super.onCleared()
    }
}

private data class SequentialTimerStateData(
    val items: List<Task>,
    val currentItemIndex: Int,
    val timeLeft: Duration,
    val accumulatedDuration: Duration,
)