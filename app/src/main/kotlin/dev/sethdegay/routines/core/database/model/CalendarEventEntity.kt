package dev.sethdegay.routines.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.sethdegay.routines.core.model.CalendarEvent
import dev.sethdegay.routines.core.model.Routine
import kotlin.time.Duration
import kotlin.time.Instant

@Entity(
    tableName = "calendar_event",
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routine_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class CalendarEventEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "start_timestamp")
    val start: Instant,

    @ColumnInfo(name = "end_timestamp")
    val end: Instant,

    @ColumnInfo(name = "duration")
    val duration: Duration,

    @ColumnInfo(name = "routine_id")
    val routineId: String,
)

fun CalendarEventEntity.asExternalModel(routine: Routine): CalendarEvent = CalendarEvent(
    id = id,
    start = start,
    end = end,
    duration = duration,
    routine = routine,
)