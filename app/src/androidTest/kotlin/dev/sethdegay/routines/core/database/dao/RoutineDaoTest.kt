package dev.sethdegay.routines.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.sethdegay.routines.core.database.RoutinesDatabase
import dev.sethdegay.routines.core.database.model.RoutineEntity
import dev.sethdegay.routines.core.database.model.TaskEntity
import dev.sethdegay.routines.core.model.RoutineType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.Uuid

@RunWith(AndroidJUnit4::class)
class RoutineDaoTest {

    private lateinit var database: RoutinesDatabase
    private lateinit var dao: RoutineDao
    private val r1Id: String = Uuid.random().toHexDashString()
    private val r2Id: String = Uuid.random().toHexDashString()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RoutinesDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.routineDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun getRoutines_returnsSuccessfully() = runTest {
        insertRoutines()
        val routines = dao.getRoutines().first()

        assertEquals(2, routines.size)
        assertNotNull(routines.filter { it.routineEntity.id == r1Id }.getOrNull(0))
        assertEquals(
            RoutineType.WORKOUT(
                warmUpDuration = 5.minutes,
                restDuration = 30.seconds,
                coolDownDuration = 60.seconds
            ),
            routines.filter { it.routineEntity.id == r1Id }[0].routineEntity.routineType,
        )
        assertEquals(3, routines.filter { it.routineEntity.id == r1Id }[0].taskEntities.size)

        assertNotNull(routines.filter { it.routineEntity.id == r2Id }.getOrNull(0))
        assertEquals(
            RoutineType.GENERIC,
            routines.filter { it.routineEntity.id == r2Id }[0].routineEntity.routineType,
        )
        assertEquals(0, routines.filter { it.routineEntity.id == r2Id }[0].taskEntities.size)
    }

    @Test
    fun deleteRoutines_deletesRelatedTasks() = runTest {
        insertRoutines()
        var routines = dao.getRoutines().first()

        assertEquals(2, routines.size)

        val r1Entity = routines[0]
        assertEquals(3, r1Entity.taskEntities.size)
        dao.delete(r1Entity.routineEntity)

        routines = dao.getRoutines().first()
        assertEquals(1, routines.size)
        val r1Tasks = dao._getRoutineTasks(r1Entity.routineEntity.id)
        assertEquals(0, r1Tasks.size)
    }

    private suspend fun insertRoutines() {
        val r1Instant = Clock.System.now()
        val r1Entity = RoutineEntity(
            id = r1Id,
            title = "R1",
            routineType = RoutineType.WORKOUT(
                warmUpDuration = 5.minutes,
                restDuration = 30.seconds,
                coolDownDuration = 60.seconds
            ),
            dateCreated = r1Instant,
            dateModified = r1Instant,
        )
        val r1Tasks = listOf(
            TaskEntity(
                id = Uuid.random().toHexDashString(),
                title = "I1",
                duration = 1.minutes,
                routineId = r1Id,
            ),
            TaskEntity(
                id = Uuid.random().toHexDashString(),
                title = "I2",
                duration = 2.minutes,
                routineId = r1Id,
            ),
            TaskEntity(
                id = Uuid.random().toHexDashString(),
                title = "I3",
                duration = 3.minutes,
                routineId = r1Id,
            ),
        )
        dao.upsertRoutineWithTasks(r1Entity, r1Tasks)

        val r2Instant = Clock.System.now()
        val r2Entity = RoutineEntity(
            id = r2Id,
            title = "R2",
            routineType = RoutineType.GENERIC,
            dateCreated = r2Instant,
            dateModified = r2Instant,
        )
        dao.upsertRoutineWithTasks(r2Entity)
    }
}