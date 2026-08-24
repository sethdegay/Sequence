package dev.sethdegay.sequence.feature.calendarlogs.impl

import dev.sethdegay.sequence.core.model.CalendarEvent

sealed interface CalendarLogsUiState {
    data object Loading : CalendarLogsUiState

    data class Success(override val events: List<CalendarEvent>) : CalendarLogsUiState

    fun showLoadingIndicator(): Boolean = this is Loading

    val events: List<CalendarEvent>
        get() = emptyList()
}