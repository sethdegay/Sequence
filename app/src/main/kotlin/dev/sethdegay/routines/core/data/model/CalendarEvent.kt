package dev.sethdegay.routines.core.data.model

import dev.sethdegay.routines.core.database.model.CalendarEventEntity
import dev.sethdegay.routines.core.model.CalendarEvent

fun CalendarEvent.asEntity(): CalendarEventEntity = CalendarEventEntity(
    id = id,
    start = start,
    end = end,
    duration = duration,
    routineId = routine.id,
)