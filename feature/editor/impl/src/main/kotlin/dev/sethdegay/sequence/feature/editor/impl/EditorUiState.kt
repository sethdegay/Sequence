package dev.sethdegay.sequence.feature.editor.impl

import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.model.Sequence

sealed interface EditorUiState {
    data object Loading : EditorUiState

    data class Success(
        override val sequence: Sequence,
        override val showSegmentEditorSheet: Boolean = false,
        override val activeSegment: Segment? = null,
    ) : EditorUiState

    fun showLoadingScreen(): Boolean = this is Loading

    val sequence: Sequence? get() = null

    val showSegmentEditorSheet: Boolean get() = false

    val activeSegment: Segment? get() = null
}