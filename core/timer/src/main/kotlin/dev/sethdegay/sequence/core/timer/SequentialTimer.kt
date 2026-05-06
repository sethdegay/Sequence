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

    fun start(
        items: List<T>,
        currentItemIndex: Int = 0,
        timeLeft: Duration? = null,
        accumulatedDuration: Duration = Duration.ZERO,
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
                var completedItemsDuration = accumulatedDuration
                for (i in currentItemIndex..items.lastIndex) {
                    val element = items[i]

                    val providedDuration = durationProvider(element)
                    if (providedDuration.inWholeMilliseconds % 1000L != 0L) {
                        _state.value =
                            SequentialTimerState.Error(IllegalArgumentException("Duration must be in whole seconds."))
                        return@launch
                    }

                    val currentItemDuration = if (i == currentItemIndex && timeLeft != null) {
                        timeLeft
                    } else {
                        providedDuration
                    }

                    countdownFlow(
                        duration = currentItemDuration,
                        dispatcher = dispatcher,
                    ).collect { timeLeft ->
                        val currentProgress = completedItemsDuration + (providedDuration - timeLeft)
                        _state.value = SequentialTimerState.Running(
                            items = items,
                            currentItemIndex = i,
                            timeLeft = timeLeft,
                            accumulatedDuration = currentProgress,
                        )
                    }

                    completedItemsDuration += providedDuration
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
        @Suppress("UNCHECKED_CAST")
        val currentState = _state.value as SequentialTimerState.Active<T>
        val nextIndex = currentState.currentItemIndex + 1

        if (nextIndex > currentState.items.lastIndex) {
            _state.value =
                SequentialTimerState.Error(IllegalStateException("Cannot move to next element. Current index is at the end."))
            return
        }

        start(
            items = currentState.items,
            currentItemIndex = nextIndex,
            timeLeft = null,
            accumulatedDuration = currentState.getCompletedDuration(nextIndex),
        )
    }

    fun movePrevious() {
        @Suppress("UNCHECKED_CAST")
        val currentState = _state.value as SequentialTimerState.Active<T>
        val nextIndex = currentState.currentItemIndex - 1

        if (nextIndex < 0) {
            _state.value =
                SequentialTimerState.Error(IllegalStateException("Cannot move to previous element. Current index is at the start."))
            return
        }

        start(
            items = currentState.items,
            currentItemIndex = nextIndex,
            timeLeft = null,
            accumulatedDuration = currentState.getCompletedDuration(nextIndex),
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
