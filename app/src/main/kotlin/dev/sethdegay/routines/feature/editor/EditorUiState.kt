package dev.sethdegay.routines.feature.editor

import dev.sethdegay.routines.core.model.Routine

sealed interface EditorUiState {
    data object Loading : EditorUiState

    data class Success(val routine: Routine) : EditorUiState

    fun showLoadingScreen(): Boolean = this is Loading
}