package dev.sethdegay.sequence.feature.timer

import dev.sethdegay.sequence.core.designsystem.component.ProgressIndicatorAmplitudeLevel
import dev.sethdegay.sequence.core.model.Step
import kotlin.time.Duration

sealed interface TimerUiState {
    data object Loading : TimerUiState

    data object Finished : TimerUiState

    data class Success(
        val currentStep: Step,
        val remainingTime: Duration,
        val progress: Float,
        val isTimerRunning: Boolean,
        val canMovePrevious: Boolean,
        val canMoveNext: Boolean,
        val amplitudeLevel: ProgressIndicatorAmplitudeLevel,
    ) : TimerUiState

    fun showLoadingScreen(): Boolean = this is Loading

    fun shouldNavigateUp(): Boolean = this is Finished
}