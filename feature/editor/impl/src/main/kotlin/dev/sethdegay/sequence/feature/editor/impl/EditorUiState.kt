package dev.sethdegay.sequence.feature.editor.impl

import dev.sethdegay.sequence.core.model.Sequence
import dev.sethdegay.sequence.core.model.Step

sealed interface EditorUiState {
    data object Loading : EditorUiState

    data class Success(
        override val sequence: Sequence,
        override val showStepEditorSheet: Boolean = false,
        override val activeStep: Step? = null,
    ) : EditorUiState

    fun showLoadingScreen(): Boolean = this is Loading

    val sequence: Sequence? get() = null

    val showStepEditorSheet: Boolean get() = false

    val activeStep: Step? get() = null
}