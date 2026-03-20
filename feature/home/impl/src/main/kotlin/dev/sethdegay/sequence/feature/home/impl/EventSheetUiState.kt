package dev.sethdegay.sequence.feature.home.impl

import dev.sethdegay.sequence.core.model.CalendarEvent

sealed interface EventSheetUiState {
    data object Loading : EventSheetUiState

    data class Success(override val events: List<CalendarEvent>) : EventSheetUiState

    fun showLoadingIndicator(): Boolean = this is Loading

    val events: List<CalendarEvent>
        get() = emptyList()
}