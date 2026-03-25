package dev.sethdegay.sequence.feature.editor_sequence.impl

sealed interface SequenceEditorEffect {
    data object Finished : SequenceEditorEffect
}