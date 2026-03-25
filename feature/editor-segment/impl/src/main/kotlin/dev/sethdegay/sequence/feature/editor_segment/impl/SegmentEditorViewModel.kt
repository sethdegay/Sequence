package dev.sethdegay.sequence.feature.editor_segment.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.sequence.core.data.repository.SegmentRepository
import dev.sethdegay.sequence.core.model.Segment
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = SegmentEditorViewModel.Factory::class)
class SegmentEditorViewModel @AssistedInject constructor(
    @Assisted("segmentId") private val segmentId: Uuid?,
    @Assisted("sequenceId") private val sequenceId: Uuid,
    @Assisted val lastSegmentPosition: Int?,
    private val segmentRepository: SegmentRepository,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("segmentId") segmentId: Uuid?,
            @Assisted("sequenceId") sequenceId: Uuid,
            @Assisted lastSegmentPosition: Int?,
        ): SegmentEditorViewModel
    }

    private val _effects = Channel<SegmentEditorEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private val _uiState = MutableStateFlow<SegmentEditorUiState>(SegmentEditorUiState.Loading)

    val uiState: StateFlow<SegmentEditorUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SegmentEditorUiState.Loading,
        )

    fun saveSegment(segment: Segment) {
        viewModelScope.launch {
            _uiState.value = SegmentEditorUiState.Loading
            segmentRepository.saveSegment(segment, sequenceId)
            _effects.send(SegmentEditorEffect.Finished)
        }
    }

    init {
        if (segmentId != null) {
            viewModelScope.launch {
                _uiState.update {
                    SegmentEditorUiState.Success(segmentRepository.getSegment(segmentId))
                }
            }
        } else {
            _uiState.update {
                val emptySegment = Segment(
                    title = "",
                    duration = Duration.ZERO,
                    order = lastSegmentPosition?.plus(1) ?: 0,
                )
                SegmentEditorUiState.Success(emptySegment)
            }
        }
    }
}