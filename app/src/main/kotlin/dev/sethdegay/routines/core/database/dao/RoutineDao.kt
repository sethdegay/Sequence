package dev.sethdegay.routines.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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
    suspend fun getRoutine(id: Long): RoutineWithTasks

    @Query("SELECT * FROM routine")
    fun _getRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM task WHERE routine_id = :id ORDER BY list_order ASC")
    suspend fun _getRoutineTasks(id: Long): List<TaskEntity>

    fun getRoutines(): Flow<List<RoutineWithTasks>> = _getRoutines().map { routineEntities ->
        routineEntities.map { routineEntity ->
            RoutineWithTasks(
                routineEntity = routineEntity,
                taskEntities = _getRoutineTasks(routineEntity.id!!),
            )
        }
    }

    @Insert
    suspend fun _insertRoutine(routineEntity: RoutineEntity): Long

    @Update
    suspend fun _updateRoutine(routineEntity: RoutineEntity)

    @Upsert
    suspend fun _upsertTask(taskEntity: TaskEntity)

    @Transaction
    suspend fun upsertRoutineWithTasks(
        routineEntity: RoutineEntity,
        taskEntities: List<TaskEntity>? = null,
    ) {
        val routineId = if (routineEntity.id == null) {
            _insertRoutine(routineEntity)
        } else {
            _updateRoutine(routineEntity)
            routineEntity.id
        }

        taskEntities?.forEach { taskEntity ->
            val taskEntityWithRoutineId = if (taskEntity.routineId == null) {
                taskEntity.copy(routineId = routineId)
            } else {
                taskEntity
            }
            _upsertTask(taskEntityWithRoutineId)
        }
    }

    @Delete
    suspend fun delete(routineEntity: RoutineEntity)
}