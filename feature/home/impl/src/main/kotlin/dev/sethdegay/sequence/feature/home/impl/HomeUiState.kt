package dev.sethdegay.sequence.feature.home.impl

import dev.sethdegay.sequence.core.model.HeatMapLevel
import dev.sethdegay.sequence.core.model.Sequence
import java.time.LocalDate
import kotlin.uuid.Uuid

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Success(
        override val sequences: List<Sequence>,
        override val activeSequenceId: Uuid?,
        override val heatMapData: Map<LocalDate, HeatMapLevel>,
        override val heatMapCalendarStart: LocalDate,
        override val heatMapCalendarEnd: LocalDate,
    ) : HomeUiState

    fun showLoadingScreen(): Boolean = this is Loading

    val heatMapData: Map<LocalDate, HeatMapLevel> get() = emptyMap()

    val sequences: List<Sequence> get() = emptyList()

    val activeSequenceId: Uuid? get() = null

    val heatMapCalendarStart: LocalDate get() = LocalDate.now()

    val heatMapCalendarEnd: LocalDate get() = LocalDate.now()
}