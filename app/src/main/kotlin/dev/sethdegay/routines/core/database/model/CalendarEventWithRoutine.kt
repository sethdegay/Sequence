package dev.sethdegay.routines.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.sethdegay.routines.core.model.CalendarEvent

data class CalendarEventWithRoutine(
    @Embedded
    val calendarEventEntity: CalendarEventEntity,

    @Relation(
        parentColumn = "routine_id",
        entityColumn = "id",
    )
    val routineEntity: RoutineEntity,
)

fun CalendarEventWithRoutine.asExternalModel(): CalendarEvent =
    calendarEventEntity.asExternalModel(routineEntity.asExternalModel(tasks = emptyList()))