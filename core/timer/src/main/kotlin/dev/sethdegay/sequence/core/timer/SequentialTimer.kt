package dev.sethdegay.sequence.core.timer

import android.os.SystemClock
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SequentialTimer<T>(
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val durationProvider: (T) -> Duration,
    private val timeProvider: () -> Long = { SystemClock.elapsedRealtime() },
) {
    companion object {
        internal suspend fun startCountdown(
            duration: Duration,
            dispatcher: CoroutineDispatcher,
            timeProvider: () -> Long,
            output: (Duration) -> Unit,
        ) = withContext(dispatcher) {
            val start = timeProvider()
            val total = duration.inWholeSeconds

            for (i in total downTo 1) {
                output(i.seconds)
                val elapsed = timeProvider() - start
                val nextDelay = (total - i + 1) * 1_000L
                val actualDelay = nextDelay - elapsed
                if (actualDelay > 0) {
                    delay(actualDelay)
                }
            }
            output(0.seconds)
        }
    }

    private val _state = MutableStateFlow<SequentialTimerState>(SequentialTimerState.Idle)
    val state: StateFlow<SequentialTimerState> = _state.asStateFlow()

    private var timerJob: Job? = null

    fun start(
        items: List<T>,
        currentItemIndex: Int = 0,
        timeLeft: Duration? = null,
        accumulatedDuration: Duration = Duration.ZERO,
        currentRound: Int = 1,
        rounds: Int = 1,
    ) {
        timerJob?.cancel()

        if (items.isEmpty() || currentItemIndex !in items.indices || (timeLeft != null && timeLeft <= 0.seconds)) {
            _state.value =
                SequentialTimerState.Error(IllegalArgumentException("Invalid list, start index, or initial delay"))
            return
        }

        if (timeLeft != null && timeLeft.inWholeMilliseconds % 1000L != 0L) {
            _state.value =
                SequentialTimerState.Error(IllegalArgumentException("Duration must be in whole seconds."))
            return
        }

        timerJob = scope.launch {
            try {
                var activeStartIndex = currentItemIndex
                var activeTimeLeft = timeLeft
                for (round in currentRound..rounds) {
                    var activeAccumulatedDuration =
                        if (round == currentRound) accumulatedDuration else Duration.ZERO
                    for (i in activeStartIndex..items.lastIndex) {
                        val element = items[i]

                        val providedDuration = durationProvider(element)
                        if (providedDuration.inWholeMilliseconds % 1000L != 0L) {
                            _state.value =
                                SequentialTimerState.Error(IllegalArgumentException("Duration must be in whole seconds."))
                            return@launch
                        }

                        val currentItemDuration = activeTimeLeft ?: providedDuration

                        startCountdown(
                            duration = currentItemDuration,
                            dispatcher = dispatcher,
                            timeProvider = timeProvider,
                        ) { timeLeft ->
                            val currentProgress =
                                activeAccumulatedDuration + (providedDuration - timeLeft)
                            _state.value = SequentialTimerState.Running(
                                items = items,
                                currentItemIndex = i,
                                timeLeft = timeLeft,
                                accumulatedDuration = currentProgress,
                                currentRound = round,
                                rounds = rounds,
                            )
                        }

                        activeAccumulatedDuration += providedDuration
                        activeTimeLeft = null
                    }
                    activeStartIndex = 0
                }
                _state.value = SequentialTimerState.Finished
            } catch (_: CancellationException) {
            }
        }
    }

    fun resume() {
        if (_state.value is SequentialTimerState.Paused<*>) {
            @Suppress("UNCHECKED_CAST")
            val currentState = _state.value as SequentialTimerState.Paused<T>
            return start(
                items = currentState.items,
                currentItemIndex = currentState.currentItemIndex,
                timeLeft = currentState.timeLeft.takeIf { it > Duration.ZERO },
                accumulatedDuration = currentState.accumulatedDuration,
                currentRound = currentState.currentRound,
                rounds = currentState.rounds,
            )
        }
    }

    fun pause() {
        if (_state.value is SequentialTimerState.Running<*>) {
            timerJob?.cancel()
            @Suppress("UNCHECKED_CAST")
            val currentState = _state.value as SequentialTimerState.Running<T>
            _state.value = SequentialTimerState.Paused(
                items = currentState.items,
                currentItemIndex = currentState.currentItemIndex,
                timeLeft = currentState.timeLeft,
                accumulatedDuration = currentState.accumulatedDuration,
                currentRound = currentState.currentRound,
                rounds = currentState.rounds,
            )
        }
    }

    fun moveNext() {
        if (_state.value !is SequentialTimerState.Active<*>) {
            _state.value =
                SequentialTimerState.Error(IllegalStateException("Cannot move element from state: Idle"))
            return
        }
        @Suppress("UNCHECKED_CAST")
        val current = _state.value as SequentialTimerState.Active<T>
        var nextIndex = current.currentItemIndex + 1
        var nextRound = current.currentRound

        if (nextIndex > current.items.lastIndex) {
            if (nextRound < current.rounds) {
                nextRound += 1
                nextIndex = 0
            } else {
                _state.value =
                    SequentialTimerState.Error(IllegalStateException("Cannot move to next element. Current index is at the end."))
                return
            }
        }

        start(
            items = current.items,
            currentItemIndex = nextIndex,
            timeLeft = null,
            accumulatedDuration = current.getCompletedDuration(nextIndex),
            currentRound = nextRound,
            rounds = current.rounds,
        )
    }

    fun movePrevious() {
        if (_state.value !is SequentialTimerState.Active<*>) {
            _state.value =
                SequentialTimerState.Error(IllegalStateException("Cannot move element from state: Idle"))
            return
        }
        @Suppress("UNCHECKED_CAST")
        val current = _state.value as SequentialTimerState.Active<T>
        var prevIndex = current.currentItemIndex - 1
        var prevRound = current.currentRound

        if (prevIndex < 0) {
            if (prevRound > 1) {
                prevRound -= 1
                prevIndex = current.items.lastIndex
            } else {
                _state.value =
                    SequentialTimerState.Error(IllegalStateException("Cannot move to previous element. Current index is at the start."))
                return
            }
        }

        start(
            items = current.items,
            currentItemIndex = prevIndex,
            timeLeft = null,
            accumulatedDuration = current.getCompletedDuration(prevIndex),
            currentRound = prevRound,
            rounds = current.rounds,
        )
    }

    fun stop() {
        timerJob?.cancel()
        _state.value = SequentialTimerState.Idle
    }

    private fun SequentialTimerState.Active<T>.getCompletedDuration(index: Int): Duration {
        return items.take(index)
            .fold(Duration.ZERO) { acc, item ->
                acc + durationProvider(item)
            }
    }
}
