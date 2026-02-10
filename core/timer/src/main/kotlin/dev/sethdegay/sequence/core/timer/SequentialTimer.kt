package dev.sethdegay.sequence.core.timer

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class SequentialTimer<T>(
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val durationProvider: (T) -> Duration,
) {
    companion object {
        internal fun countdownFlow(
            duration: Duration,
            dispatcher: CoroutineDispatcher,
        ): Flow<Duration> = flow {
            (duration.inWholeSeconds * 1_000L downTo 1_000L step 1_000L).forEach {
                emit(it.milliseconds)
                delay(1_000L)
            }
            emit(0.seconds)
        }.flowOn(dispatcher)
    }

    private val _state = MutableStateFlow<SequentialTimerState>(SequentialTimerState.Idle)
    val state: StateFlow<SequentialTimerState> = _state.asStateFlow()

    private var timerJob: Job? = null

    fun start(items: List<T>, startIndex: Int = 0, timeLeft: Duration? = null) {
        timerJob?.cancel()

        if (items.isEmpty() || startIndex !in items.indices || (timeLeft != null && timeLeft <= 0.seconds)) {
            _state.value =
                SequentialTimerState.Error(IllegalArgumentException("Invalid list, start index, or initial delay"))
            return
        }

        if (timeLeft != null && timeLeft.inWholeMilliseconds % 1000L != 0L) {
            _state.value =
                SequentialTimerState.Error(IllegalArgumentException("Duration must be in whole seconds."))
            return
        }

        var accumulatedDuration = items.take(startIndex)
            .fold(Duration.ZERO) { acc, item ->
                acc + durationProvider(item)
            }

        timerJob = scope.launch {
            try {
                for (i in startIndex..items.lastIndex) {
                    val element = items[i]

                    val providedDuration = durationProvider(element)
                    val duration = if (i == startIndex && timeLeft != null) {
                        timeLeft
                    } else {
                        if (providedDuration.inWholeMilliseconds % 1000L != 0L) {
                            _state.value =
                                SequentialTimerState.Error(IllegalArgumentException("Duration must be in whole seconds."))
                            return@launch
                        }
                        providedDuration
                    }

                    countdownFlow(
                        duration = duration,
                        dispatcher = dispatcher,
                    ).collect { timeLeft ->
                        _state.value = SequentialTimerState.Running(
                            items = items,
                            currentItemIndex = i,
                            timeLeft = timeLeft,
                            accumulatedDuration = accumulatedDuration + (providedDuration - timeLeft),
                        )
                    }

                    accumulatedDuration += duration
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
                startIndex = currentState.currentItemIndex,
                timeLeft = if (currentState.timeLeft > 0.seconds) {
                    currentState.timeLeft
                } else {
                    null
                }
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
            )
        }
    }

    fun moveNext() {
        val (items, currentIndex) = getCurrentStateData() ?: return

        if (currentIndex + 1 > items.lastIndex) {
            _state.value =
                SequentialTimerState.Error(IllegalStateException("Cannot move to next element. Current index is at the end."))
            return
        }

        start(items = items, startIndex = currentIndex + 1, timeLeft = null)
    }

    fun movePrevious() {
        val (items, currentIndex) = getCurrentStateData() ?: return

        if (currentIndex - 1 < 0) {
            _state.value =
                SequentialTimerState.Error(IllegalStateException("Cannot move to previous element. Current index is at the start."))
            return
        }

        start(items = items, startIndex = currentIndex - 1, timeLeft = null)
    }

    fun stop() {
        timerJob?.cancel()
        _state.value = SequentialTimerState.Idle
    }

    private fun getCurrentStateData(): Pair<List<T>, Int>? {
        return when (val currentState = _state.value) {
            is SequentialTimerState.Paused<*> -> {
                @Suppress("UNCHECKED_CAST")
                currentState.items as List<T> to currentState.currentItemIndex
            }

            is SequentialTimerState.Running<*> -> {
                @Suppress("UNCHECKED_CAST")
                currentState.items as List<T> to currentState.currentItemIndex
            }

            else -> {
                _state.value =
                    SequentialTimerState.Error(IllegalStateException("Cannot move element from state: $currentState"))
                null
            }
        }
    }
}
