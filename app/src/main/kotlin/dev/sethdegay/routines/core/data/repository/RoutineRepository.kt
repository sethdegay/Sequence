package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.model.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineRepository {
    suspend fun getRoutine(id: Long): Routine

    fun getRoutines(): Flow<List<Routine>>

    suspend fun saveRoutine(routine: Routine)

    suspend fun delete(routine: Routine)
}