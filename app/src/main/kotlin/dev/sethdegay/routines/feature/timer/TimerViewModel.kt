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
import dev.sethdegay.routines.core.model.Task
import dev.sethdegay.routines.core.timer.SequentialTimer
import dev.sethdegay.routines.core.timer.SequentialTimerState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    val uiState: StateFlow<TimerUiState> = timer.state.map { it.asTimerUiState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimerUiState.Loading,
    )

    init {
        viewModelScope.launch {
            timer.start(routineRepository.getRoutine(id).tasks)
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

        val (items, index, time) = when (this) {
            is SequentialTimerState.Running<*> -> Triple(items, currentItemIndex, timeLeft)
            is SequentialTimerState.Paused<*> -> Triple(items, currentItemIndex, timeLeft)
        }

        @Suppress("UNCHECKED_CAST")
        val tasks = items as List<Task>
        val currentTask = tasks[index]

        val progress = if (tasks.lastIndex > 0) {
            index.toFloat() / tasks.lastIndex
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