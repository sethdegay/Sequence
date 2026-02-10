package dev.sethdegay.routines.core.data.model

import dev.sethdegay.sequence.core.database.model.CalendarEventEntity
import dev.sethdegay.sequence.core.model.CalendarEvent

fun CalendarEvent.asEntity(): CalendarEventEntity = CalendarEventEntity(
    id = id,
    start = start,
    end = end,
    duration = duration,
    sequenceId = sequence.id,
)