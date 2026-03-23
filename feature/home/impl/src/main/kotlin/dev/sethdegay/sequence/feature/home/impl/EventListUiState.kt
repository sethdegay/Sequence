package dev.sethdegay.sequence.feature.home.impl

import dev.sethdegay.sequence.core.model.CalendarEvent

sealed interface EventListUiState {
    data object Loading : EventListUiState

    data class Success(override val events: List<CalendarEvent>) : EventListUiState

    fun showLoadingIndicator(): Boolean = this is Loading

    val events: List<CalendarEvent>
        get() = emptyList()
}