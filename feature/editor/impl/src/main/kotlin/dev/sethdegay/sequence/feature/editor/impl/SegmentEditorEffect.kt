package dev.sethdegay.sequence.feature.editor.impl

sealed interface SegmentEditorEffect {
    data object Finished : SegmentEditorEffect
}