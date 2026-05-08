package dev.sethdegay.sequence.feature.sequence.contextmenu.impl

import kotlin.time.Instant

sealed interface SequenceContextMenuUiState {
    data object Loading : SequenceContextMenuUiState

    data class Success(
        override val title: String,
        override val dateCreated: Instant,
        override val dateModified: Instant,
    ) : SequenceContextMenuUiState

    val title: String? get() = null

    val dateCreated: Instant? get() = null

    val dateModified: Instant? get() = null
}