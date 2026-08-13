package dev.sethdegay.sequence.feature.sequence.contextmenu.impl

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.sethdegay.sequence.core.data.repository.SegmentRepository
import dev.sethdegay.sequence.core.data.repository.SequenceRepository
import dev.sethdegay.sequence.core.data.repository.UserPreferencesRepository
import dev.sethdegay.sequence.core.model.Sequence
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val SEQUENCE_ID_KEY = "sequenceId"
private const val LIBRARY_ID_KEY = "libraryId"

@OptIn(ExperimentalUuidApi::class)
@HiltViewModel(assistedFactory = SequenceContextMenuViewModel.Factory::class)
class SequenceContextMenuViewModel @AssistedInject constructor(
    @Assisted(SEQUENCE_ID_KEY) private val sequenceId: Uuid,
    @Assisted(LIBRARY_ID_KEY) private val libraryId: Uuid,
    private val sequenceRepository: SequenceRepository,
    private val segmentRepository: SegmentRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(SEQUENCE_ID_KEY) sequenceId: Uuid,
            @Assisted(LIBRARY_ID_KEY) libraryId: Uuid,
        ): SequenceContextMenuViewModel
    }

    private val _showDeleteConfirmationDialog = MutableStateFlow(false)
    private val _sequence: MutableStateFlow<Sequence?> = MutableStateFlow(null)
    val uiState: StateFlow<SequenceContextMenuUiState> =
        combine(
            _sequence,
            _showDeleteConfirmationDialog,
        ) { sequence, showDeleteConfirmationDialog ->
            if (sequence == null) {
                return@combine SequenceContextMenuUiState.Loading
            }
            SequenceContextMenuUiState.Success(
                title = sequence.title,
                dateCreated = sequence.dateCreated,
                dateModified = sequence.dateModified,
                showDeleteConfirmationDialog = showDeleteConfirmationDialog,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SequenceContextMenuUiState.Loading,
        )

    private val _effects = Channel<SequenceContextMenuEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        viewModelScope.launch { _sequence.value = sequenceRepository.getSequence(sequenceId) }
    }

    fun getIds(): Pair<Uuid, Uuid> = Pair(sequenceId, libraryId)

    fun delete() {
        val sequence = _sequence.getAndUpdate { null } ?: return
        viewModelScope.launch {
            sequenceRepository.delete(sequence, libraryId)
            userPreferencesRepository.clearActiveSequenceIdIfMatches(sequence.id)
            _effects.send(SequenceContextMenuEffect.Finished)
        }
    }

    fun duplicate() {
        val sequence = _sequence.getAndUpdate { null } ?: return
        viewModelScope.launch {
            createDuplicateSequence(sequence)
            _effects.send(SequenceContextMenuEffect.Finished)
        }
    }

    fun setShowDeleteConfirmationDialog(showDeleteConfirmationDialog: Boolean) {
        _showDeleteConfirmationDialog.value = showDeleteConfirmationDialog
    }

    private suspend fun createDuplicateSequence(sequence: Sequence) {
        val newId = Uuid.random()
        val newTitle = context.getString(R.string.duplicate_title_template, sequence.title)
        val now = Clock.System.now()

        val newSequence = sequence.copy(
            id = newId,
            title = newTitle,
            dateCreated = now,
            dateModified = now,
            segments = emptyList(),
        )
        val newSegments = sequence.segments.map { it.copy(id = Uuid.random()) }

        sequenceRepository.saveSequence(newSequence, libraryId)
        segmentRepository.saveSegments(newSegments, newId)
        userPreferencesRepository.setActiveSequenceId(newId)
    }
}
