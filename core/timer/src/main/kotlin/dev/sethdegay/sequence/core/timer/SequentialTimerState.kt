package dev.sethdegay.sequence.core.timer

import kotlin.time.Duration

sealed interface SequentialTimerState {
    data object Idle : SequentialTimerState
    data class Running<T>(
        val items: List<T>,
        val currentItemIndex: Int,
        val timeLeft: Duration,
        val accumulatedDuration: Duration,
    ) : SequentialTimerState

    data class Paused<T>(
        val items: List<T>,
        val currentItemIndex: Int,
        val timeLeft: Duration,
        val accumulatedDuration: Duration,
    ) : SequentialTimerState

    data object Finished : SequentialTimerState
    data class Error(val exception: Throwable) : SequentialTimerState
}
