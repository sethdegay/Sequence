package dev.sethdegay.sequence.feature.segment.editor.impl

import dev.sethdegay.sequence.core.model.Segment
import dev.sethdegay.sequence.core.model.SegmentInputMethod

sealed interface SegmentEditorUiState {
    data object Loading : SegmentEditorUiState

    data class Success(
        override val segment: Segment,
        override val inputMethod: SegmentInputMethod,
    ) : SegmentEditorUiState

    val segment: Segment?
        get() = null

    val inputMethod: SegmentInputMethod
        get() = SegmentInputMethod.PICK
}