package dev.sethdegay.routines.core.data.model

import dev.sethdegay.routines.core.database.model.RoutineEntity
import dev.sethdegay.routines.core.model.Routine

fun Routine.asEntity(): RoutineEntity = RoutineEntity(
    id = id,
    title = title,
    description = description,
    routineType = routineType,
    dateCreated = dateCreated,
    dateModified = dateModified,
)