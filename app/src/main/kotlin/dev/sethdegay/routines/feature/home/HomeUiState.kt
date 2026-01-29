package dev.sethdegay.routines.feature.home

import dev.sethdegay.routines.core.model.HeatMapLevel
import dev.sethdegay.routines.core.model.Routine
import java.time.LocalDate

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        override val routines: List<Routine>,
        override val routinesAccordionExpandedId: String?,
        override val heatMapData: Map<LocalDate, HeatMapLevel>,
        override val heatMapCalendarStart: LocalDate,
        override val heatMapCalendarEnd: LocalDate,
    ) : HomeUiState

    fun showLoadingScreen(): Boolean = this is Loading

    val heatMapData: Map<LocalDate, HeatMapLevel> get() = emptyMap()

    val routines: List<Routine> get() = emptyList()

    val routinesAccordionExpandedId: String? get() = null

    val heatMapCalendarStart: LocalDate get() = LocalDate.now()

    val heatMapCalendarEnd: LocalDate get() = LocalDate.now()
}