package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.model.CalendarEvent
import dev.sethdegay.routines.core.model.HeatMapLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlin.time.Instant

interface CalendarEventRepository {
    fun getCalendarEvents(start: Instant, end: Instant): Flow<List<CalendarEvent>>

    fun getHeatMapData(start: Instant, end: Instant): Flow<Map<LocalDate, HeatMapLevel>>

    suspend fun insertCalendarEvent(calendarEvent: CalendarEvent)
}