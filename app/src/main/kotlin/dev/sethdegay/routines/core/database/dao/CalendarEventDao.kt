package dev.sethdegay.routines.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import dev.sethdegay.routines.core.database.model.CalendarEventEntity
import dev.sethdegay.routines.core.database.model.CalendarEventWithSequence
import dev.sethdegay.routines.core.database.model.DailyEventCount
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

@Dao
interface CalendarEventDao {
    @Transaction
    @Query("SELECT * FROM calendar_event WHERE start_timestamp BETWEEN :start AND :end")
    fun getCalendarEvents(start: Instant, end: Instant): Flow<List<CalendarEventWithSequence>>

    @Query(
        """
        SELECT
            strftime('%Y-%m-%d', start_timestamp, 'unixepoch', 'localtime') AS date,
            COUNT(id) AS count
        FROM calendar_event
        WHERE start_timestamp BETWEEN :start AND :end
        GROUP BY date
        ORDER BY date ASC
    """
    )
    fun getDailyEventCount(start: Instant, end: Instant): Flow<List<DailyEventCount>>

    @Insert
    suspend fun insertCalendarEvent(calendarEventEntity: CalendarEventEntity)
}