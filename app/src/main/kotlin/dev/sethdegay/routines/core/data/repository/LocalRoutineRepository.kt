package dev.sethdegay.routines.core.data.repository

import dev.sethdegay.routines.core.data.model.asEntity
import dev.sethdegay.routines.core.database.dao.RoutineDao
import dev.sethdegay.routines.core.database.model.asExternalModel
import dev.sethdegay.routines.core.model.Routine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Duration

class LocalRoutineRepository @Inject constructor(
    private val routineDao: RoutineDao,
) : RoutineRepository {
    override suspend fun getRoutine(id: String): Routine =
        routineDao.getRoutine(id).asExternalModel()

    override fun getRoutines(): Flow<List<Routine>> = routineDao.getRoutines()
        .map { routines -> routines.map { it.asExternalModel() } }

    override suspend fun saveRoutine(routine: Routine) {
        routineDao.upsertRoutineWithTasks(
            routineEntity = routine.copy(
                totalDuration = routine.tasks.fold(Duration.ZERO) { acc, task ->
                    acc + task.duration
                },
            ).asEntity(),
            taskEntities = routine.tasks.mapIndexed { i, task ->
                task.copy(order = i + 1)
                    .asEntity(routineId = routine.id)
            },
        )
    }

    override suspend fun delete(routine: Routine) {
        routineDao.delete(routine.asEntity())
    }
}