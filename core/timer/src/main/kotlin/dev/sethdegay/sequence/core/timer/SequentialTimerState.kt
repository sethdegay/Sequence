package dev.sethdegay.sequence.core.timer

import kotlin.time.Duration

sealed interface SequentialTimerState {
    data object Idle : SequentialTimerState
    data object Finished : SequentialTimerState
    data class Error(val exception: Throwable) : SequentialTimerState

    data class Running<T>(
        override val items: List<T>,
        override val currentItemIndex: Int,
        override val timeLeft: Duration,
        override val accumulatedDuration: Duration,
        override val currentRound: Int,
        override val rounds: Int,
    ) : Active<T>

    data class Paused<T>(
        override val items: List<T>,
        override val currentItemIndex: Int,
        override val timeLeft: Duration,
        override val accumulatedDuration: Duration,
        override val currentRound: Int,
        override val rounds: Int,
    ) : Active<T>

    sealed interface Active<T> : SequentialTimerState {
        val items: List<T>
        val currentItemIndex: Int
        val timeLeft: Duration
        val accumulatedDuration: Duration
        val currentRound: Int
        val rounds: Int
    }
}
