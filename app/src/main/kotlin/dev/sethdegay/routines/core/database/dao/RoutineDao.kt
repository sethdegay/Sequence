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

@Suppress("FunctionName")
@Dao
interface RoutineDao {

    @Query("SELECT * FROM routine")
    suspend fun _getRoutines(): List<RoutineEntity>

    @Query("SELECT * FROM interval WHERE routine_id = :id ORDER BY list_order ASC")
    suspend fun _getRoutineIntervals(id: Long): List<IntervalEntity>

    @Transaction
    suspend fun getRoutines(): List<RoutineWithIntervals> =
        _getRoutines().map {
            RoutineWithIntervals(
                routineEntity = it,
                intervalEntities = _getRoutineIntervals(it.id!!),
            )
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