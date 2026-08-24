package dev.sethdegay.sequence.feature.segmenteditor.impl

sealed interface SegmentEditorEffect {
    data object Finished : SegmentEditorEffect
}