package dev.sethdegay.sequence.feature.editor_segment.impl

sealed interface SegmentEditorEffect {
    data object Finished : SegmentEditorEffect
}