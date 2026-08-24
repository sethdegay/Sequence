package dev.sethdegay.sequence.feature.sequenceeditor.impl

sealed interface SequenceEditorEffect {
    data object Finished : SequenceEditorEffect
}