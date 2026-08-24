package dev.sethdegay.sequence.feature.segmenteditor.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.sequence.core.data.repository.SegmentRepository
import dev.sethdegay.sequence.core.data.repository.UserPreferencesRepository
import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.model.SegmentInputMethod
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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
    private val userPreferencesRepository: UserPreferencesRepository,
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

    private val _segment = MutableStateFlow<Segment?>(null)
    private val _inputMethod = userPreferencesRepository.uiState.map { it.activeSegmentIm }

    private val _showDeleteConfirmationDialog = MutableStateFlow(false)

    val uiState: StateFlow<SegmentEditorUiState> =
        combine(
            _segment,
            _inputMethod,
            _showDeleteConfirmationDialog,
        ) { segment, inputMethod, showDeleteConfirmationDialog ->
            when (segment) {
                null -> SegmentEditorUiState.Loading
                else -> SegmentEditorUiState.Success(
                    segment = segment,
                    inputMethod = inputMethod,
                    showDeleteConfirmationDialog = showDeleteConfirmationDialog,
                )
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SegmentEditorUiState.Loading,
            )

    fun saveSegment(segment: Segment) {
        viewModelScope.launch {
            _segment.value = null
            segmentRepository.saveSegment(segment, sequenceId)
            _effects.send(SegmentEditorEffect.Finished)
        }
    }

    fun deleteSegment(segment: Segment) {
        viewModelScope.launch {
            _segment.value = null
            segmentRepository.deleteSegment(segment, sequenceId)
            _effects.send(SegmentEditorEffect.Finished)
        }
    }

    fun onInputMethodChange(inputMethod: SegmentInputMethod) {
        viewModelScope.launch { userPreferencesRepository.setActiveSegmentIm(inputMethod) }
    }

    fun setShowDeleteConfirmationDialog(showDeleteConfirmationDialog: Boolean) {
        _showDeleteConfirmationDialog.value = showDeleteConfirmationDialog
    }

    init {
        if (segmentId != null) {
            viewModelScope.launch {
                _segment.update { segmentRepository.getSegment(segmentId) }
            }
        } else {
            val emptySegment = Segment(
                title = "",
                duration = Duration.ZERO,
                order = lastSegmentPosition?.plus(1) ?: 0,
            )
            _segment.update { emptySegment }
        }
    }
}