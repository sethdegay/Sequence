package dev.sethdegay.routines.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.routines.core.data.repository.CalendarEventRepository
import dev.sethdegay.routines.core.data.repository.RoutineRepository
import dev.sethdegay.routines.core.data.repository.UserPreferencesRepository
import dev.sethdegay.routines.core.model.HeatMapLevel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Instant
import kotlinx.datetime.LocalDate as KotlinLocalDate
import java.time.LocalDate as JavaLocalDate

@HiltViewModel
class HomeViewModel @Inject constructor(
    routineRepository: RoutineRepository,
    calendarEventRepository: CalendarEventRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val timeZone = TimeZone.currentSystemDefault()

    private val currentDateTime = Clock.System.now().toLocalDateTime(timeZone)
    private val firstDayOfTheYear =
        LocalDateTime(currentDateTime.year, 1, 1, 0, 0).toInstant(timeZone)
    private val endOfCurrentDay = currentDateTime.date.atEndOfDayIn(timeZone)

    private val heatMapCalendarStart: JavaLocalDate = firstDayOfTheYear
        .toLocalDateTime(timeZone)
        .date
        .toJavaLocalDate()

    private val heatMapCalendarEnd: JavaLocalDate = endOfCurrentDay
        .toLocalDateTime(timeZone)
        .date
        .toJavaLocalDate()

    val uiState: StateFlow<HomeUiState> = combine(
        routineRepository.getRoutines(),
        userPreferencesRepository.uiState,
        calendarEventRepository.getHeatMapData(
            start = firstDayOfTheYear,
            end = endOfCurrentDay,
        ).map { it.toJavaHeatMapData() },
    ) { routines, uiState, heatMapData ->
        HomeUiState.Success(
            routines = routines,
            routinesAccordionExpandedId = uiState.routinesAccordionExpandedId,
            heatMapData = heatMapData,
            heatMapCalendarStart = heatMapCalendarStart,
            heatMapCalendarEnd = heatMapCalendarEnd,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState.Loading,
    )

    fun setRoutinesAccordionExpandedId(routinesAccordionExpandedId: String?) {
        viewModelScope.launch {
            userPreferencesRepository.setRoutinesAccordionExpandedId(routinesAccordionExpandedId)
        }
    }

    fun setActiveCalendarEventSheetDate(date: JavaLocalDate) {
    }
}

private fun KotlinLocalDate.atEndOfDayIn(timeZone: TimeZone): Instant =
    atStartOfDayIn(timeZone).plus(1.days).minus(1.nanoseconds)

private fun Map<KotlinLocalDate, HeatMapLevel>.toJavaHeatMapData(): Map<JavaLocalDate, HeatMapLevel> =
    mapKeys { entry -> entry.key.toJavaLocalDate() }