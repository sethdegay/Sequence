package dev.sethdegay.routines.core.data.model

import dev.sethdegay.routines.core.database.model.TaskEntity
import dev.sethdegay.routines.core.model.Task

fun Task.asEntity(routineId: Long?): TaskEntity = TaskEntity(
    id = id,
    title = title,
    duration = duration,
    order = order,
    routineId = routineId,
)