package dev.sethdegay.routines.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import dev.sethdegay.routines.core.model.Routine

data class RoutineWithTasks(
    @Embedded
    val routineEntity: RoutineEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "routine_id",
    )
    val taskEntities: List<TaskEntity>,
)

fun RoutineWithTasks.asExternalModel(): Routine =
    routineEntity.asExternalModel(taskEntities.map { it.asExternalModel() })