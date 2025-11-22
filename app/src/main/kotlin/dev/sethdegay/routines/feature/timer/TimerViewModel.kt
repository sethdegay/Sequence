package dev.sethdegay.routines.feature.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.routines.core.designsystem.component.ProgressIndicatorAmplitudeLevel
import dev.sethdegay.routines.core.designsystem.component.TimerControlsActions
import dev.sethdegay.routines.core.designsystem.component.TimerControlsMode
import dev.sethdegay.routines.core.model.Task
import dev.sethdegay.routines.core.timer.SequentialTimer
import dev.sethdegay.routines.core.timer.SequentialTimerState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds


@HiltViewModel(assistedFactory = TimerViewModel.Factory::class)
class TimerViewModel @AssistedInject constructor(
    @Assisted private val id: Long,
    private val timer: SequentialTimer<Task>,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: Long): TimerViewModel
    }

    val uiState: StateFlow<TimerUiState> = timer.state.map { it.asTimerUiState() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TimerUiState.Loading,
    )

    init {
        timer.start(
            items = listOf(
                Task(title = "A", duration = 3.seconds),
                Task(title = "B", duration = 5.seconds),
                Task(title = "C", duration = 2.seconds),
            ),
        )
    }

    private fun SequentialTimerState.asTimerUiState(): TimerUiState {
        @Suppress("UNCHECKED_CAST")
        return when (this) {
            SequentialTimerState.Idle -> TimerUiState.Loading
            SequentialTimerState.Finished -> TimerUiState.Finished
            is SequentialTimerState.Error -> TimerUiState.Loading // TODO implement error state
            is SequentialTimerState.Running<*> -> TimerUiState.Success(
                currentItem = Pair(
                    first = (items as List<Task>)[currentItemIndex],
                    second = timeLeft,
                ),
                controlsMode = TimerControlsMode.RUNNING,
                controlsActions = TimerControlsActions(
                    onMainButtonClick = { timer.pause() },
                    onPreviousButtonClick = { timer.movePrevious() },
                    onNextButtonClick = { timer.moveNext() },
                    enablePreviousButton = currentItemIndex != 0,
                    enableNextButton = currentItemIndex != items.lastIndex,
                ),
                progress = currentItemIndex.toFloat() / items.lastIndex,
                amplitudeLevel = ProgressIndicatorAmplitudeLevel.MAXIMUM,
            )

            is SequentialTimerState.Paused<*> -> TimerUiState.Success(
                currentItem = Pair(
                    first = (items as List<Task>)[currentItemIndex],
                    second = timeLeft,
                ),
                controlsMode = TimerControlsMode.PAUSED,
                controlsActions = TimerControlsActions(
                    onMainButtonClick = { timer.resume() },
                    onPreviousButtonClick = { timer.movePrevious() },
                    onNextButtonClick = { timer.moveNext() },
                    enablePreviousButton = currentItemIndex != 0,
                    enableNextButton = currentItemIndex != items.lastIndex,
                ),
                progress = currentItemIndex.toFloat() / items.lastIndex,
                amplitudeLevel = ProgressIndicatorAmplitudeLevel.MAXIMUM,
            )
        }
    }
}