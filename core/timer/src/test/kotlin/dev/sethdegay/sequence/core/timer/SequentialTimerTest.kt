package dev.sethdegay.sequence.core.timer

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class SequentialTimerTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() = testScope.runTest {
        val timer = createTimer()
        assertEquals(SequentialTimerState.Idle, timer.state.value)
    }

    @Test
    fun `timer runs sequentially through items and finishes`() = testScope.runTest {
        val items = listOf("Task A", "Task B")
        val timer = createTimer { if (it == "Task A") 2.seconds else 1.seconds }

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())

            timer.start(items)

            with(awaitItem() as SequentialTimerState.Running<*>) {
                assertEquals("Task A", items[currentItemIndex])
                assertEquals(2.seconds, timeLeft)
            }

            testScope.advanceTimeBy(1000)

            with(awaitItem() as SequentialTimerState.Running<*>) {
                assertEquals("Task A", items[currentItemIndex])
                assertEquals(1.seconds, timeLeft)
            }

            testScope.advanceTimeBy(1000)

            assertEquals(0.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            with(awaitItem() as SequentialTimerState.Running<*>) {
                assertEquals("Task B", items[currentItemIndex])
                assertEquals(1.seconds, timeLeft)
            }

            testScope.advanceTimeBy(1000)

            assertEquals(0.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            assertEquals(SequentialTimerState.Finished, awaitItem())
        }
    }

    @Test
    fun `timer pauses and saves state correctly`() = testScope.runTest {
        val items = listOf("Item 1")
        val timer = createTimer { 5.seconds }

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())

            timer.start(items)

            assertEquals(5.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            testScope.advanceTimeBy(1000)
            assertEquals(4.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            testScope.advanceTimeBy(1000)
            assertEquals(3.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            timer.pause()

            val pausedState = awaitItem() as SequentialTimerState.Paused<*>
            assertEquals(3.seconds, pausedState.timeLeft)
            assertEquals(0, pausedState.currentItemIndex)

            testScope.advanceTimeBy(10000)
            expectNoEvents()
        }
    }

    @Test
    fun `timer resumes from paused state`() = testScope.runTest {
        val items = listOf("Item 1")
        val timer = createTimer { 10.seconds }

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())

            timer.start(items)

            assertEquals(10.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            testScope.advanceTimeBy(1000)
            assertEquals(9.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            testScope.advanceTimeBy(1000)
            assertEquals(8.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            timer.pause()

            val initialPaused = awaitItem() as SequentialTimerState.Paused<*>
            assertEquals(8.seconds, initialPaused.timeLeft)

            timer.resume()

            val resumedRunning = awaitItem() as SequentialTimerState.Running<*>
            assertEquals(8.seconds, resumedRunning.timeLeft)
            assertEquals(0, resumedRunning.currentItemIndex)

            testScope.advanceTimeBy(1000)
            assertEquals(7.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `start with fractional duration causes error`() = testScope.runTest {
        val fractionalItems = listOf("A", "B")
        val fractionalTimer = createTimer { 5.5.seconds }

        val wholeTimer = createTimer { 5.seconds }

        fractionalTimer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())
            fractionalTimer.start(fractionalItems)

            val errorState = awaitItem()
            assertTrue(errorState is SequentialTimerState.Error)
            assertEquals(
                "Duration must be in whole seconds.",
                (errorState as SequentialTimerState.Error).exception.message
            )
            expectNoEvents()
        }

        wholeTimer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())
            wholeTimer.start(fractionalItems, timeLeft = 4.seconds + 500.milliseconds)

            val errorState = awaitItem()
            assertTrue(errorState is SequentialTimerState.Error)
            assertEquals(
                "Duration must be in whole seconds.",
                (errorState as SequentialTimerState.Error).exception.message
            )
            expectNoEvents()
        }
    }

    @Test
    fun `start handles invalid inputs gracefully`() = testScope.runTest {
        val timer = createTimer()

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())

            timer.start(emptyList())
            assertTrue(awaitItem() is SequentialTimerState.Error)

            timer.start(listOf("A"), currentItemIndex = 5)
            assertTrue(awaitItem() is SequentialTimerState.Error)
        }
    }

    @Test
    fun `stop resets state to Idle`() = testScope.runTest {
        val timer = createTimer { 10.seconds }
        timer.start(listOf("A"))

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())
            assertTrue(awaitItem() is SequentialTimerState.Running<*>)

            timer.stop()

            assertEquals(SequentialTimerState.Idle, awaitItem())
        }
    }

    @Test
    fun `moveNext jumps to next item and resets duration`() = testScope.runTest {
        val items = listOf("A", "B")
        val timer = createTimer { 5.seconds }

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())

            timer.start(items)

            with(awaitItem() as SequentialTimerState.Running<*>) {
                assertEquals(0, currentItemIndex)
                assertEquals(5.seconds, timeLeft)
            }

            testScope.advanceTimeBy(1000)
            assertEquals(4.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)
            testScope.advanceTimeBy(1000)
            assertEquals(3.seconds, (awaitItem() as SequentialTimerState.Running<*>).timeLeft)

            timer.moveNext()

            with(awaitItem() as SequentialTimerState.Running<*>) {
                assertEquals(1, currentItemIndex)
                assertEquals("B", items[currentItemIndex])
                assertEquals(5.seconds, timeLeft) // Duration reset
            }

            timer.stop()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `moveNext from Paused state works correctly`() = testScope.runTest {
        val items = listOf("A", "B")
        val timer = createTimer { 5.seconds }

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())
            timer.start(items)

            awaitItem()

            timer.pause()
            assertTrue(awaitItem() is SequentialTimerState.Paused<*>)

            timer.moveNext()

            // Should go directly to Running B (no intermediate pause event since already paused)
            with(awaitItem() as SequentialTimerState.Running<*>) {
                assertEquals(1, currentItemIndex)
                assertEquals("B", items[currentItemIndex])
                assertEquals(5.seconds, timeLeft)
            }
        }
    }

    @Test
    fun `moveNext fails when at end of list`() = testScope.runTest {
        val items = listOf("A")
        val timer = createTimer()

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())
            timer.start(items)
            awaitItem()

            timer.moveNext()

            val error = awaitItem() as SequentialTimerState.Error
            assertEquals(
                "Cannot move to next element. Current index is at the end.",
                error.exception.message
            )
        }
    }

    @Test
    fun `moveNext from invalid state errors`() = testScope.runTest {
        val timer = createTimer()
        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())
            timer.moveNext()
            val error = awaitItem() as SequentialTimerState.Error
            assertEquals("Cannot move element from state: Idle", error.exception.message)
        }
    }

    @Test
    fun `movePrevious jumps to previous item and resets duration`() = testScope.runTest {
        val items = listOf("A", "B")
        val timer = createTimer { 5.seconds }

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())

            timer.start(items, currentItemIndex = 1)

            with(awaitItem() as SequentialTimerState.Running<*>) {
                assertEquals(1, currentItemIndex)
                assertEquals("B", items[currentItemIndex])
            }

            timer.movePrevious()

            with(awaitItem() as SequentialTimerState.Running<*>) {
                assertEquals(0, currentItemIndex)
                assertEquals("A", items[currentItemIndex])
                assertEquals(5.seconds, timeLeft)
            }
        }
    }

    @Test
    fun `movePrevious from Paused state works correctly`() = testScope.runTest {
        val items = listOf("A", "B")
        val timer = createTimer { 5.seconds }

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())
            timer.start(items, currentItemIndex = 1)

            awaitItem()

            timer.pause()
            assertTrue(awaitItem() is SequentialTimerState.Paused<*>)

            timer.movePrevious()

            with(awaitItem() as SequentialTimerState.Running<*>) {
                assertEquals(0, currentItemIndex)
                assertEquals("A", items[currentItemIndex])
                assertEquals(5.seconds, timeLeft)
            }
        }
    }

    @Test
    fun `movePrevious fails when at start of list`() = testScope.runTest {
        val items = listOf("A", "B")
        val timer = createTimer()

        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())
            timer.start(items, currentItemIndex = 0)
            awaitItem()

            timer.movePrevious()

            val error = awaitItem() as SequentialTimerState.Error
            assertEquals(
                "Cannot move to previous element. Current index is at the start.",
                error.exception.message
            )
        }
    }

    @Test
    fun `movePrevious from invalid state errors`() = testScope.runTest {
        val timer = createTimer()
        timer.state.test {
            assertEquals(SequentialTimerState.Idle, awaitItem())
            timer.movePrevious()
            val error = awaitItem() as SequentialTimerState.Error
            assertEquals("Cannot move element from state: Idle", error.exception.message)
        }
    }

    private fun createTimer(
        durationProvider: (String) -> Duration = { 1.seconds },
    ): SequentialTimer<String> {
        return SequentialTimer(
            scope = testScope,
            dispatcher = testDispatcher,
            durationProvider = durationProvider,
        )
    }
}