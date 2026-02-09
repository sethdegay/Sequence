package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.data.model.asEntity
import dev.sethdegay.routines.core.database.dao.CalendarEventDao
import dev.sethdegay.routines.core.database.model.asExternalModel
import dev.sethdegay.sequence.core.model.CalendarEvent
import dev.sethdegay.sequence.core.model.HeatMapLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject
import kotlin.time.Instant

class LocalCalendarEventRepository @Inject constructor(
    private val calendarEventDao: CalendarEventDao,
) : CalendarEventRepository {
    companion object {
        private fun mapCountToHeatMapLevel(count: Int, maxCount: Int): HeatMapLevel {
            val percentage = if (maxCount == 0) 0.0 else count.toDouble() / maxCount
            return when {
                percentage == 0.0 -> HeatMapLevel.Zero
                percentage <= 0.25 -> HeatMapLevel.One
                percentage <= 0.50 -> HeatMapLevel.Two
                percentage <= 0.75 -> HeatMapLevel.Three
                else -> HeatMapLevel.Four
            }
        }
    }

    override fun getCalendarEvents(start: Instant, end: Instant): Flow<List<CalendarEvent>> =
        calendarEventDao.getCalendarEvents(start, end).map { events ->
            events.map { it.asExternalModel() }
        }

    override fun getHeatMapData(
        start: Instant,
        end: Instant,
    ): Flow<Map<LocalDate, HeatMapLevel>> {
        return calendarEventDao.getDailyEventCount(start, end).map { rawCounts ->
            val heatMap = mutableMapOf<LocalDate, HeatMapLevel>()
            val maxCount = rawCounts.maxOfOrNull { it.count } ?: 1
            rawCounts.forEach { eventCount ->
                val date = eventCount.date
                val heatMapLevel = mapCountToHeatMapLevel(eventCount.count, maxCount)
                heatMap[date] = heatMapLevel
            }
            heatMap
        }
    }

    override suspend fun insertCalendarEvent(calendarEvent: CalendarEvent) {
        calendarEventDao.insertCalendarEvent(calendarEvent.asEntity())
    }
}