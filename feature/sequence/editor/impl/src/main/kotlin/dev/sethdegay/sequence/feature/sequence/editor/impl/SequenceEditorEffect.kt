package dev.sethdegay.sequence.feature.sequence.editor.impl

sealed interface SequenceEditorEffect {
    data object Finished : SequenceEditorEffect
}