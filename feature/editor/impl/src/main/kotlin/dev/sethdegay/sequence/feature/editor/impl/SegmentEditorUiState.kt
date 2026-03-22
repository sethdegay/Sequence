package dev.sethdegay.sequence.feature.editor.impl

import dev.sethdegay.sequence.core.model.Segment

sealed interface SegmentEditorUiState {
    data object Loading : SegmentEditorUiState

    data class Success(override val segment: Segment) : SegmentEditorUiState

    fun showLoadingIndicator(): Boolean = this is Loading

    val segment: Segment?
        get() = null
}