package dev.sethdegay.sequence.feature.editor.impl

import dev.sethdegay.sequence.core.model.Sequence

sealed interface SequenceEditorUiState {
    data object Loading : SequenceEditorUiState

    data class Success(override val sequence: Sequence) : SequenceEditorUiState

    val sequence: Sequence? get() = null
}