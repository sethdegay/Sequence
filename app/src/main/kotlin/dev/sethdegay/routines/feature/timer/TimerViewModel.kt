package dev.sethdegay.routines.feature.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.routines.core.data.repository.RoutineRepository
import dev.sethdegay.routines.core.designsystem.component.ProgressIndicatorAmplitudeLevel
import dev.sethdegay.routines.core.designsystem.component.TimerControlsActions
import dev.sethdegay.routines.core.model.Routine
import dev.sethdegay.routines.core.model.Task
import dev.sethdegay.routines.core.timer.SequentialTimer
import dev.sethdegay.routines.core.timer.SequentialTimerState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration

@HiltViewModel(assistedFactory = TimerViewModel.Factory::class)
class TimerViewModel @AssistedInject constructor(
    @Assisted private val id: String,
    private val timer: SequentialTimer<Task>,
    private val routineRepository: RoutineRepository,
) : ViewModel(), TimerControlsActions {

    @AssistedFactory
    interface Factory {
        fun create(id: String): TimerViewModel
    }

    private lateinit var routine: Routine

    val uiState: StateFlow<TimerUiState> = timer.state.map { it.asTimerUiState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimerUiState.Loading,
    )

    init {
        viewModelScope.launch {
            timer.start(routineRepository.getRoutine(id).also { routine = it }.tasks)
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
            return TimerUiState.Finished
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
}

private data class SequentialTimerStateData(
    val items: List<Task>,
    val currentItemIndex: Int,
    val timeLeft: Duration,
    val accumulatedDuration: Duration,
)