package dev.sethdegay.routines.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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
    suspend fun insertRoutine(routineEntity: RoutineEntity): Long

    @Insert
    suspend fun insertInterval(intervalEntity: IntervalEntity)

    @Update
    suspend fun updateRoutine(routineEntity: RoutineEntity)

    @Update
    suspend fun updateInterval(intervalEntity: IntervalEntity)

    @Delete
    suspend fun delete(routineEntity: RoutineEntity, intervalEntities: List<IntervalEntity>)

    @Delete
    suspend fun delete(intervalEntity: IntervalEntity)
}