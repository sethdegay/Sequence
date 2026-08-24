package dev.sethdegay.sequence.feature.sequencecontextmenu.impl

sealed interface SequenceContextMenuEffect {
    data object Finished : SequenceContextMenuEffect
}