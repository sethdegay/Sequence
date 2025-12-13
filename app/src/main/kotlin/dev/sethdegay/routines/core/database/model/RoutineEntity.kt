package dev.sethdegay.routines.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.sethdegay.routines.core.model.Routine
import dev.sethdegay.routines.core.model.RoutineType
import dev.sethdegay.routines.core.model.Task
import kotlin.time.Instant

@Entity(tableName = "routine")
data class RoutineEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "routine_type")
    val routineType: RoutineType,

    @ColumnInfo(name = "date_created")
    val dateCreated: Instant,

    @ColumnInfo(name = "date_modified")
    val dateModified: Instant,
)

fun RoutineEntity.asExternalModel(tasks: List<Task>): Routine = Routine(
    id = id,
    title = title,
    description = description,
    routineType = routineType,
    dateCreated = dateCreated,
    dateModified = dateModified,
    tasks = tasks,
)
