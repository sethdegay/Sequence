package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.data.model.asEntity
import dev.sethdegay.routines.core.database.dao.RoutineDao
import dev.sethdegay.routines.core.database.model.asExternalModel
import dev.sethdegay.routines.core.model.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalRoutineRepository @Inject constructor(
    private val routineDao: RoutineDao,
) : RoutineRepository {
    override fun getRoutine(id: Long): Flow<Routine> =
        routineDao.getRoutine(id).map { it.asExternalModel() }

    override fun getRoutines(): Flow<List<Routine>> = routineDao.getRoutines()
        .map { routines -> routines.map { it.asExternalModel() } }

    override suspend fun saveRoutine(routine: Routine): Routine = routineDao.upsertRoutineWithTasks(
        routineEntity = routine.asEntity(),
        taskEntities = routine.tasks.map { it.asEntity(routineId = routine.id) },
    ).asExternalModel()

    override suspend fun delete(routine: Routine) {
        routineDao.delete(routine.asEntity())
    }
}