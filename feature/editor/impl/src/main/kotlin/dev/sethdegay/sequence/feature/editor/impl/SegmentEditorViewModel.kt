package dev.sethdegay.sequence.feature.editor.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.sequence.core.data.repository.SegmentRepository
import dev.sethdegay.sequence.core.model.Segment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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

    private val emptySegment = Segment(
        title = "",
        duration = Duration.ZERO,
        order = lastSegmentPosition?.plus(1) ?: 0,
    )

    private val _uiState = MutableStateFlow(segmentId)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<SegmentEditorUiState> = _uiState.flatMapLatest { id ->
        if (id != null) {
            segmentRepository.getSegment(id).map { SegmentEditorUiState.Success(it) }
        } else {
            flowOf(SegmentEditorUiState.Loading)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SegmentEditorUiState.Loading,
    )

    fun onSegmentUpdate(segment: Segment) {
        viewModelScope.launch {
            segmentRepository.saveSegment(segment, sequenceId)
            _uiState.update { segment.id }
        }
    }

    init {
        if (_uiState.value == null) {
            onSegmentUpdate(emptySegment)
        }
    }
}