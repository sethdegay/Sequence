package dev.sethdegay.sequence.feature.editor.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.sequence.core.data.repository.SequenceRepository
import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.model.Step
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@HiltViewModel(assistedFactory = EditorViewModel.Factory::class)
class EditorViewModel @AssistedInject constructor(
    @Assisted("sequence") private val id: Uuid?,
    @Assisted("workspace") private val workspaceId: Uuid,
    private val sequenceRepository: SequenceRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("sequence") id: Uuid?,
            @Assisted("workspace") workspaceId: Uuid,
        ): EditorViewModel
    }

    private val emptySequence = with(Clock.System.now()) {
        Sequence(
            title = "",
            description = "",
            dateCreated = this,
            dateModified = this,
            steps = emptyList(),
            totalDuration = Duration.ZERO,
        )
    }

    private val _editableUiState = MutableStateFlow<EditorUiState.Success?>(null)

    val uiState: StateFlow<EditorUiState> = _editableUiState
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EditorUiState.Loading,
        )

    fun onTitleSave(title: String) {
        _editableUiState.update { it?.copy(sequence = it.sequence.copy(title = title)) }
    }

    fun onDescriptionSave(description: String) {
        _editableUiState.update { it?.copy(sequence = it.sequence.copy(description = description)) }
    }

    fun showStepEditor(step: Step?) {
        _editableUiState.update { it?.copy(showStepEditorSheet = true, activeStep = step) }
    }

    fun hideStepEditor() {
        _editableUiState.update { it?.copy(showStepEditorSheet = false, activeStep = null) }
    }

    fun onStepsSave(steps: List<Step>) {
        _editableUiState.update {
            it?.copy(
                sequence = it.sequence.copy(
                    steps = steps,
                    dateModified = Clock.System.now(),
                )
            )
        }
    }

    fun onStepSave(step: Step) {
        _editableUiState.update { state ->
            if (state == null) return@update state
            val updatedSteps = if (state.activeStep == null) {
                state.sequence.steps + step
            } else {
                state.sequence.steps.map { if (state.activeStep.id == it.id) step else it }
            }
            EditorUiState.Success(
                sequence = state.sequence.copy(
                    steps = updatedSteps,
                    dateModified = Clock.System.now(),
                ),
                showStepEditorSheet = false,
                activeStep = null,
            )
        }
    }

    init {
        viewModelScope.launch {
            _editableUiState.debounce(250.milliseconds)
                .filterNotNull()
                .map { it.sequence }
                .distinctUntilChanged()
                .filter { it != emptySequence }
                .collect { updateAndSaveSequence(it) }
        }

        if (id != null) {
            viewModelScope.launch {
                _editableUiState.value = EditorUiState.Success(sequenceRepository.getSequence(id))
            }
        } else {
            _editableUiState.value = EditorUiState.Success(emptySequence)
        }
    }

    private suspend fun updateAndSaveSequence(sequence: Sequence) {
        val reorderedSteps = sequence.steps.mapIndexed { i, step ->
            step.copy(order = i + 1)
        }

        val totalDuration = reorderedSteps.fold(Duration.ZERO) { acc, task ->
            acc + task.duration
        }

        val updatedSequence = sequence.copy(
            steps = reorderedSteps,
            totalDuration = totalDuration,
        )

        _editableUiState.update {
            it?.copy(sequence = updatedSequence)
        }

        sequenceRepository.saveSequence(updatedSequence, workspaceId)
    }
}