package dev.sethdegay.routines.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.sethdegay.routines.core.database.SequenceDatabase
import dev.sethdegay.routines.core.database.model.SequenceEntity
import dev.sethdegay.routines.core.database.model.StepEntity
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

    private lateinit var database: SequenceDatabase
    private lateinit var dao: SequenceDao
    private val r1Id: String = Uuid.random().toHexDashString()
    private val r2Id: String = Uuid.random().toHexDashString()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SequenceDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.sequenceDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun getRoutines_returnsSuccessfully() = runTest {
        insertRoutines()
        val routines = dao.getSequences().first()

        assertEquals(2, routines.size)
        assertNotNull(routines.filter { it.sequenceEntity.id == r1Id }.getOrNull(0))
        assertEquals(3, routines.filter { it.sequenceEntity.id == r1Id }[0].stepEntities.size)

        assertNotNull(routines.filter { it.sequenceEntity.id == r2Id }.getOrNull(0))
        assertEquals(0, routines.filter { it.sequenceEntity.id == r2Id }[0].stepEntities.size)
    }

    @Test
    fun deleteRoutines_deletesRelatedTasks() = runTest {
        insertRoutines()
        var routines = dao.getSequences().first()

        assertEquals(2, routines.size)

        val r1Entity = routines[0]
        assertEquals(3, r1Entity.stepEntities.size)
        dao.delete(r1Entity.sequenceEntity)

        routines = dao.getSequences().first()
        assertEquals(1, routines.size)
        val r1Tasks = dao._getSequenceSteps(r1Entity.sequenceEntity.id)
        assertEquals(0, r1Tasks.size)
    }

    private suspend fun insertRoutines() {
        val r1Instant = Clock.System.now()
        val r1Entity = SequenceEntity(
            id = r1Id,
            title = "R1",
            description = "",
            dateCreated = r1Instant,
            dateModified = r1Instant,
            totalDuration = 6.minutes,
        )
        val r1Tasks = listOf(
            StepEntity(
                id = Uuid.random().toHexDashString(),
                title = "I1",
                duration = 1.minutes,
                sequenceId = r1Id,
            ),
            StepEntity(
                id = Uuid.random().toHexDashString(),
                title = "I2",
                duration = 2.minutes,
                sequenceId = r1Id,
            ),
            StepEntity(
                id = Uuid.random().toHexDashString(),
                title = "I3",
                duration = 3.minutes,
                sequenceId = r1Id,
            ),
        )
        dao.upsertSequenceWithSteps(r1Entity, r1Tasks)

        val r2Instant = Clock.System.now()
        val r2Entity = SequenceEntity(
            id = r2Id,
            title = "R2",
            description = "",
            dateCreated = r2Instant,
            dateModified = r2Instant,
            totalDuration = 0.seconds,
        )
        dao.upsertSequenceWithSteps(r2Entity)
    }
}