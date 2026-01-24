package dev.sethdegay.routines.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.sethdegay.routines.core.database.model.RoutineEntity
import dev.sethdegay.routines.core.database.model.RoutineWithTasks
import dev.sethdegay.routines.core.database.model.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("FunctionName")
@Dao
interface RoutineDao {
    @Query("SELECT * FROM routine WHERE id = :id")
    suspend fun _getRoutine(id: String): RoutineEntity

    suspend fun getRoutine(id: String): RoutineWithTasks = RoutineWithTasks(
        routineEntity = _getRoutine(id),
        taskEntities = _getRoutineTasks(id),
    )

    @Query("SELECT * FROM routine")
    fun _getRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM task WHERE routine_id = :id ORDER BY list_order ASC")
    suspend fun _getRoutineTasks(id: String): List<TaskEntity>

    fun getRoutines(): Flow<List<RoutineWithTasks>> = _getRoutines().map { routineEntities ->
        routineEntities.map { routineEntity ->
            RoutineWithTasks(
                routineEntity = routineEntity,
                taskEntities = _getRoutineTasks(routineEntity.id),
            )
        }
    }

    @Upsert
    suspend fun _upsertRoutine(routineEntity: RoutineEntity)

    @Upsert
    suspend fun _upsertTasks(taskEntities: List<TaskEntity>)

    @Transaction
    suspend fun upsertRoutineWithTasks(
        routineEntity: RoutineEntity,
        taskEntities: List<TaskEntity>? = null,
    ) {
        _upsertRoutine(routineEntity)
        if (taskEntities != null) {
            _upsertTasks(taskEntities)
        }
    }

    @Delete
    suspend fun delete(routineEntity: RoutineEntity)
}