package dev.sethdegay.sequence.feature.sequence.contextmenu.impl

sealed interface SequenceContextMenuEffect {
    data object Finished : SequenceContextMenuEffect
}