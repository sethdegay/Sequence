package dev.sethdegay.routines.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import dev.sethdegay.routines.core.database.model.IntervalEntity
import dev.sethdegay.routines.core.database.model.RoutineEntity
import dev.sethdegay.routines.core.database.model.RoutineWithIntervals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("FunctionName")
@Dao
interface RoutineDao {

    @Query("SELECT * FROM routine")
    fun _getRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM interval WHERE routine_id = :id ORDER BY list_order ASC")
    suspend fun _getRoutineIntervals(id: Long): List<IntervalEntity>

    fun getRoutines(): Flow<List<RoutineWithIntervals>> = _getRoutines().map { routineEntities ->
        routineEntities.map { routineEntity ->
            RoutineWithIntervals(
                routineEntity = routineEntity,
                intervalEntities = _getRoutineIntervals(routineEntity.id!!),
            )
        }
    }

    @Insert
    suspend fun _insertRoutine(routineEntity: RoutineEntity): Long

    @Update
    suspend fun _updateRoutine(routineEntity: RoutineEntity)

    @Upsert
    suspend fun _upsertInterval(intervalEntity: IntervalEntity)

    @Transaction
    suspend fun upsertRoutineWithIntervals(
        routineEntity: RoutineEntity,
        intervalEntities: List<IntervalEntity>? = null,
    ) {
        val routineId = if (routineEntity.id == null) {
            _insertRoutine(routineEntity)
        } else {
            _updateRoutine(routineEntity)
            routineEntity.id
        }

        intervalEntities?.forEach { intervalEntity ->
            val intervalEntityWithRoutineId = if (intervalEntity.routineId == null) {
                intervalEntity.copy(routineId = routineId)
            } else {
                intervalEntity
            }
            _upsertInterval(intervalEntityWithRoutineId)
        }
    }

    @Delete
    suspend fun delete(routineEntity: RoutineEntity)
}