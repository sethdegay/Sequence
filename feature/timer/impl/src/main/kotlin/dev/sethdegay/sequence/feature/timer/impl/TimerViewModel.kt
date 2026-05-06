package dev.sethdegay.sequence.feature.timer.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.sequence.core.audio.SfxManager
import dev.sethdegay.sequence.core.audio.TtsManager
import dev.sethdegay.sequence.core.data.repository.CalendarEventRepository
import dev.sethdegay.sequence.core.data.repository.SequenceRepository
import dev.sethdegay.sequence.core.data.repository.UserPreferencesRepository
import dev.sethdegay.sequence.core.designsystem.component.ProgressIndicatorAmplitudeLevel
import dev.sethdegay.sequence.core.designsystem.component.TimerControlsActions
import dev.sethdegay.sequence.core.model.CalendarEvent
import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.model.Settings
import dev.sethdegay.sequence.core.timer.SequentialTimer
import dev.sethdegay.sequence.core.timer.SequentialTimerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@HiltViewModel(assistedFactory = TimerViewModel.Factory::class)
class TimerViewModel @AssistedInject constructor(
    @Assisted private val id: Uuid,
    private val timer: SequentialTimer<Segment>,
    private val ttsManager: TtsManager,
    private val sfxManager: SfxManager,
    private val sequenceRepository: SequenceRepository,
    private val calendarEventRepository: CalendarEventRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel(), TimerControlsActions {
    @AssistedFactory
    interface Factory {
        fun create(id: Uuid): TimerViewModel
    }

    private lateinit var sequence: Sequence
    private lateinit var start: Instant
    private lateinit var saveCalendarEventJob: Job

    private val _uiState = MutableStateFlow<TimerUiState>(TimerUiState.Loading)
    val uiState: StateFlow<TimerUiState>
        get() = _uiState

    init {
        viewModelScope.launch {
            val (tickSound, completionSound, speakTitle) = userPreferencesRepository
                .settings
                .first()
                .asAudioSettings()
            timer.state.collectLatest { state ->
                _uiState.update {
                    state.asTimerUiState(
                        speakTitle = { if (speakTitle) ttsManager.speak(it) },
                        playOddTickSound = { if (tickSound) sfxManager.playTickOdd() },
                        playEvenTickSound = { if (tickSound) sfxManager.playTickEven() },
                        playCompletionSound = { if (completionSound) sfxManager.playBell() },
                    )
                }
            }
        }
        viewModelScope.launch {
            combine(
                ttsManager.initialize(),
                sfxManager.initialize(),
            ) { tts, sfx -> tts && sfx }
                .distinctUntilChanged()
                .first()
                .apply {
                    if (!this) return@apply
                    sequence = sequenceRepository.getSequence(id)
                    timer.start(items = sequence.segments, rounds = sequence.rounds)
                    start = Clock.System.now()
                }
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

    private fun SequentialTimerState.asTimerUiState(
        speakTitle: (String) -> Unit,
        playOddTickSound: () -> Unit,
        playEvenTickSound: () -> Unit,
        playCompletionSound: () -> Unit,
    ): TimerUiState {
        if (this is SequentialTimerState.Idle || this is SequentialTimerState.Error) {
            return TimerUiState.Loading
        }
        if (this is SequentialTimerState.Finished) {
            saveCalendarEvent()
            return TimerUiState.Loading
        }

        this as SequentialTimerState.Active<*>

        val currentSegment = items[currentItemIndex] as Segment

        val progress = if (sequence.totalDuration > Duration.ZERO) {
            (accumulatedDuration / sequence.totalDuration).toFloat()
        } else {
            1.0f
        }

        val isTimerRunning = this is SequentialTimerState.Running<*>

        if (isTimerRunning) {
            if (currentSegment.duration == timeLeft) {
                speakTitle(currentSegment.title)
            }
            when (timeLeft) {
                5.seconds, 3.seconds, 1.seconds -> playOddTickSound()
                4.seconds, 2.seconds -> playEvenTickSound()
                0.seconds -> playCompletionSound()
            }
        }

        return TimerUiState.Success(
            currentSegment = currentSegment,
            remainingTime = timeLeft,
            isTimerRunning = isTimerRunning,
            canMovePrevious = currentRound > 1 || currentItemIndex > 0,
            canMoveNext = currentRound < rounds || currentItemIndex < items.lastIndex,
            progress = progress,
            amplitudeLevel = if (isTimerRunning) {
                ProgressIndicatorAmplitudeLevel.MAXIMUM
            } else {
                ProgressIndicatorAmplitudeLevel.FLAT
            },
            topAppBarTitle = sequence.title,
            currentRound = currentRound.takeIf { rounds > 1 },
        )
    }

    private fun saveCalendarEvent(timeout: Duration = 10.seconds) {
        if (::saveCalendarEventJob.isInitialized && saveCalendarEventJob.isActive) {
            return
        }
        val now = Clock.System.now()
        val calendarEvent = CalendarEvent(
            start = start,
            end = now,
            duration = now - start,
            sequence = sequence,
        )
        saveCalendarEventJob = viewModelScope.launch {
            try {
                withTimeout(timeout) {
                    calendarEventRepository.insertCalendarEvent(calendarEvent)
                }
            } catch (_: TimeoutCancellationException) {
                // TODO handle error
            } finally {
                _uiState.update { TimerUiState.Finished }
            }
        }
    }

    override fun onCleared() {
        ttsManager.release()
        sfxManager.release()
        if (::saveCalendarEventJob.isInitialized) {
            saveCalendarEventJob.cancel()
        }
        super.onCleared()
    }
}

private data class AudioSettings(
    val tickSound: Boolean,
    val completionSound: Boolean,
    val speakTitle: Boolean,
)

private fun Settings.asAudioSettings() = AudioSettings(
    tickSound = !muteAll && tickSound,
    completionSound = !muteAll && completionSound,
    speakTitle = !muteAll && speakTitle,
)