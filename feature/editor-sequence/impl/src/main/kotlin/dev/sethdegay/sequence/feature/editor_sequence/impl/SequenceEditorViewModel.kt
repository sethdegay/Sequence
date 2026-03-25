package dev.sethdegay.sequence.feature.editor_sequence.impl

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.sequence.core.data.repository.SegmentRepository
import dev.sethdegay.sequence.core.data.repository.SequenceRepository
import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.model.Sequence
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = SequenceEditorViewModel.Factory::class)
class SequenceEditorViewModel @AssistedInject constructor(
    @Assisted("sequenceId") private val sequenceId: Uuid?,
    @Assisted("workspaceId") private val workspaceId: Uuid,
    private val sequenceRepository: SequenceRepository,
    private val segmentRepository: SegmentRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("sequenceId") sequenceId: Uuid?,
            @Assisted("workspaceId") workspaceId: Uuid,
        ): SequenceEditorViewModel
    }

    private val now = Clock.System.now()

    private val id = sequenceId ?: Uuid.random()
    private val title = TextFieldState()
    private val description = TextFieldState()
    private var dateCreated = now
    private var dateModified = now

    private val localSegments = MutableStateFlow<List<Segment>?>(null)

    private val currentSegments = combine(
        segmentRepository.getSegments(id),
        localSegments,
    ) { repo, local ->
        if (local == null) repo else {
            val repoMap = repo.associateBy { it.id }
            val reconciled = local.mapNotNull { repoMap[it.id] }
            val newSegments = repo.filter { it.id !in local.map { s -> s.id }.toSet() }
            reconciled + newSegments
        }
    }.distinctUntilChanged()

    private val currentDuration = currentSegments
        .map { list -> list.sumOf { it.duration.inWholeMilliseconds }.milliseconds }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Duration.ZERO,
        )

    val uiState: StateFlow<SequenceEditorUiState> = combine(
        currentSegments,
        currentDuration,
    ) { segments, duration ->
        SequenceEditorUiState.Success(
            title = title,
            description = description,
            segments = segments,
            totalDuration = duration,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SequenceEditorUiState.Loading,
    )

    private val _effects = Channel<SequenceEditorEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onSegmentOrderChanged(newOrder: List<Segment>) {
        val updatedOrder = newOrder.mapIndexed { index, segment ->
            segment.copy(order = index + 1)
        }
        localSegments.value = updatedOrder
        viewModelScope.launch {
            try {
                segmentRepository.saveSegments(updatedOrder, id)
            } catch (_: Exception) {
                localSegments.value = null
            }
        }
    }

    fun getSequenceId(): Uuid = id

    fun requestExit() {
        val sequence = construct()
        viewModelScope.launch {
            if (sequence.isEmpty()) {
                sequenceRepository.delete(sequence, workspaceId)
            }
            _effects.send(SequenceEditorEffect.Finished)
        }
    }

    init {
        viewModelScope.launch {
            if (sequenceId != null) sequenceRepository.getSequence(sequenceId).deconstruct()
            snapshotFlow { construct() }
                .distinctUntilChanged()
                .debounce(500.milliseconds)
                .collectLatest { sequence ->
                    sequenceRepository.saveSequence(sequence, workspaceId)
                }
        }
    }

    private fun construct(): Sequence = Sequence(
        id = id,
        title = title.text.toString(),
        description = description.text.toString(),
        dateCreated = dateCreated,
        dateModified = dateModified,
        segments = emptyList(), // saved separately in onSegmentOrderChanged
        totalDuration = currentDuration.value,
    )

    private fun Sequence.deconstruct() {
        this@SequenceEditorViewModel.title.setTextAndPlaceCursorAtEnd(title)
        this@SequenceEditorViewModel.description.setTextAndPlaceCursorAtEnd(description)
        this@SequenceEditorViewModel.dateCreated = dateCreated
        this@SequenceEditorViewModel.dateModified = dateModified
        this@SequenceEditorViewModel.localSegments.value = null
    }

    private fun Sequence.isEmpty(): Boolean =
        sequenceId == null
                && title.isEmpty()
                && description.isEmpty()
                && totalDuration == Duration.ZERO
}