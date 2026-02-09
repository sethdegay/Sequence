package dev.sethdegay.routines.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.sethdegay.sequence.core.model.CalendarEvent

data class CalendarEventWithSequence(
    @Embedded
    val calendarEventEntity: CalendarEventEntity,

    @Relation(
        parentColumn = "sequence_id",
        entityColumn = "id",
    )
    val sequenceEntity: SequenceEntity,
)

fun CalendarEventWithSequence.asExternalModel(): CalendarEvent =
    calendarEventEntity.asExternalModel(sequenceEntity.asExternalModel(steps = emptyList()))