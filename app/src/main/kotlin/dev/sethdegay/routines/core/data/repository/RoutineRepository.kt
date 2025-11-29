package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.model.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    fun getRoutine(id: Long): Flow<Routine>

    fun getRoutines(): Flow<List<Routine>>

    suspend fun saveRoutine(routine: Routine): Long

    suspend fun delete(routine: Routine)
}