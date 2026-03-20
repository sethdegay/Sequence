package dev.sethdegay.sequence.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.sethdegay.sequence.core.data.repository.CalendarEventRepository
import dev.sethdegay.sequence.core.data.repository.SequenceRepository
import dev.sethdegay.sequence.core.data.repository.UserPreferencesRepository
import dev.sethdegay.sequence.core.data.repository.WorkspaceRepository
import dev.sethdegay.sequence.core.model.HeatMapLevel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.uuid.Uuid
import kotlinx.datetime.LocalDate as KotlinLocalDate
import java.time.LocalDate as JavaLocalDate

@HiltViewModel
class HomeViewModel @Inject constructor(
    sequenceRepository: SequenceRepository,
    calendarEventRepository: CalendarEventRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val workspaceRepository: WorkspaceRepository,
) : ViewModel() {

    lateinit var workspaceId: Uuid

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
        sequenceRepository.getSequences(),
        userPreferencesRepository.uiState,
        calendarEventRepository.getHeatMapData(
            start = firstDayOfTheYear,
            end = endOfCurrentDay,
        ).map { it.toJavaHeatMapData() }
            .distinctUntilChanged(),
    ) { sequences, uiState, heatMapData ->
        HomeUiState.Success(
            sequences = sequences,
            accordionExpandedId = uiState.accordionExpandedId,
            heatMapData = heatMapData,
            heatMapCalendarStart = heatMapCalendarStart,
            heatMapCalendarEnd = heatMapCalendarEnd,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState.Loading,
    )

    fun setAccordionExpandedId(accordionExpandedId: Uuid?) {
        viewModelScope.launch {
            userPreferencesRepository.setAccordionExpandedId(accordionExpandedId)
        }
    }

    fun onCalendarDateSelected(date: JavaLocalDate): ClosedRange<Instant> {
        return date.toKotlinLocalDate()
            .let { it.atStartOfDayIn(timeZone)..it.atEndOfDayIn(timeZone) }
    }

    init {
        // TODO
        viewModelScope.launch {
            delay(3.seconds)
            workspaceId = workspaceRepository.getWorkspaces().first().id
        }
    }
}

private fun KotlinLocalDate.atEndOfDayIn(timeZone: TimeZone): Instant =
    atStartOfDayIn(timeZone).plus(1.days).minus(1.nanoseconds)

private fun Map<KotlinLocalDate, HeatMapLevel>.toJavaHeatMapData(): Map<JavaLocalDate, HeatMapLevel> =
    mapKeys { entry -> entry.key.toJavaLocalDate() }