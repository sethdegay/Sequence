package dev.sethdegay.sequence.feature.timer.impl

import dev.sethdegay.sequence.core.designsystem.component.ProgressIndicatorAmplitudeLevel
import dev.sethdegay.sequence.core.model.Segment
import kotlin.time.Duration

sealed interface TimerUiState {
    data object Loading : TimerUiState

    data object Finished : TimerUiState

    data class Success(
        val currentSegment: Segment,
        val remainingTime: Duration,
        val progress: Float,
        val isTimerRunning: Boolean,
        val canMovePrevious: Boolean,
        val canMoveNext: Boolean,
        val amplitudeLevel: ProgressIndicatorAmplitudeLevel,
        override val currentRound: Int?,
        override val topAppBarTitle: String,
    ) : TimerUiState

    fun showLoadingScreen(): Boolean = this is Loading

    fun shouldNavigateUp(): Boolean = this is Finished

    val topAppBarTitle: String get() = ""

    val currentRound: Int? get() = null
}