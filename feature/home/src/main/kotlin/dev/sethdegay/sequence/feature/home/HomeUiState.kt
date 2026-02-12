package dev.sethdegay.sequence.feature.home

import dev.sethdegay.sequence.core.model.CalendarEvent
import dev.sethdegay.sequence.core.model.HeatMapLevel
import dev.sethdegay.sequence.core.model.Sequence
import java.time.LocalDate

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        override val sequences: List<Sequence>,
        override val routinesAccordionExpandedId: String?,
        override val heatMapData: Map<LocalDate, HeatMapLevel>,
        override val heatMapCalendarStart: LocalDate,
        override val heatMapCalendarEnd: LocalDate,
        override val showCalendarEventsSheet: Boolean,
        override val activeCalendarEvents: List<CalendarEvent>?,
    ) : HomeUiState

    fun showLoadingScreen(): Boolean = this is Loading

    val heatMapData: Map<LocalDate, HeatMapLevel> get() = emptyMap()

    val sequences: List<Sequence> get() = emptyList()

    val routinesAccordionExpandedId: String? get() = null

    val heatMapCalendarStart: LocalDate get() = LocalDate.now()

    val heatMapCalendarEnd: LocalDate get() = LocalDate.now()

    val showCalendarEventsSheet: Boolean get() = false

    val activeCalendarEvents: List<CalendarEvent>? get() = null
}