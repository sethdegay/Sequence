package dev.sethdegay.sequence.feature.editor.impl

import dev.sethdegay.sequence.core.model.Sequence

sealed interface EditorUiState {
    data object Loading : EditorUiState

    data class Success(override val sequence: Sequence) : EditorUiState

    fun showLoadingScreen(): Boolean = this is Loading

    val sequence: Sequence? get() = null
}