package dev.sethdegay.sequence.feature.editor.impl

sealed interface SequenceEditorEffect {
    data object Finished : SequenceEditorEffect
}