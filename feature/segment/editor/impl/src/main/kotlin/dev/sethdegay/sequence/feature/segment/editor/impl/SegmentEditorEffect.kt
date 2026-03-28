package dev.sethdegay.sequence.feature.segment.editor.impl

sealed interface SegmentEditorEffect {
    data object Finished : SegmentEditorEffect
}