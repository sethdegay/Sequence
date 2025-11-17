package dev.sethdegay.routines.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.sethdegay.routines.core.model.Routine

data class RoutineWithIntervals(
    @Embedded
    val routineEntity: RoutineEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "routine_id",
    )
    val intervalEntities: List<IntervalEntity>,
)

fun RoutineWithIntervals.asExternalModel(): Routine =
    routineEntity.asExternalModel(intervalEntities.map { it.asExternalModel() })