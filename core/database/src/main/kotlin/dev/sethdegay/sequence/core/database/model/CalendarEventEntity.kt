package dev.sethdegay.sequence.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.sethdegay.sequence.core.model.CalendarEvent
import dev.sethdegay.sequence.core.model.Sequence
import kotlin.time.Duration
import kotlin.time.Instant

@Entity(
    tableName = "calendar_event",
    foreignKeys = [
        ForeignKey(
            entity = SequenceEntity::class,
            parentColumns = ["id"],
            childColumns = ["sequence_id"],
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

    @ColumnInfo(name = "sequence_id")
    val sequenceId: String,
)

fun CalendarEventEntity.asExternalModel(sequence: Sequence): CalendarEvent = CalendarEvent(
    id = id,
    start = start,
    end = end,
    duration = duration,
    sequence = sequence,
)