package dev.sethdegay.routines.core.data.model

import dev.sethdegay.routines.core.database.model.IntervalEntity
import dev.sethdegay.routines.core.model.Interval

fun Interval.asEntity(routineId: Long?): IntervalEntity = IntervalEntity(
    id = id,
    title = title,
    duration = duration,
    order = order,
    routineId = routineId,
)