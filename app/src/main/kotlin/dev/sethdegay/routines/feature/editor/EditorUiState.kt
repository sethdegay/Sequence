package dev.sethdegay.routines.feature.editor

import dev.sethdegay.routines.core.model.Routine
import dev.sethdegay.routines.core.model.Task

sealed interface EditorUiState {
    data object Loading : EditorUiState

    data class Success(
        val routine: Routine,
        override val showTaskEditorSheet: Boolean = false,
        override val activeTask: Task? = null,
    ) : EditorUiState

    fun showLoadingScreen(): Boolean = this is Loading

    val showTaskEditorSheet: Boolean get() = false

    val activeTask: Task? get() = null
}