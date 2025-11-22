package dev.sethdegay.routines.feature.timer

import dev.sethdegay.routines.core.designsystem.component.ProgressIndicatorAmplitudeLevel
import dev.sethdegay.routines.core.designsystem.component.TimerControlsActions
import dev.sethdegay.routines.core.designsystem.component.TimerControlsMode
import dev.sethdegay.routines.core.model.Task
import kotlin.time.Duration

sealed interface TimerUiState {
    data object Loading : TimerUiState

    data object Finished : TimerUiState

    data class Success(
        val currentItem: Pair<Task, Duration>,
        val controlsMode: TimerControlsMode,
        val controlsActions: TimerControlsActions,
        val progress: Float,
        val amplitudeLevel: ProgressIndicatorAmplitudeLevel,
    ) : TimerUiState

    fun showLoadingScreen(): Boolean = this is Loading
}