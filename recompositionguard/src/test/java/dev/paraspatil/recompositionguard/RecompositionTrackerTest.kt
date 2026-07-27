package dev.paraspatil.recompositionguard

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class RecompositionTrackerTest {

    @Before
    fun setup(){
        RecompositionGuard.reset()
        RecompositionGuard.install(ThresholdConfig(warnThreshold = 5, errorThreshold = 10))
    }
    @Test
    fun `track increment count correctly`(){
        val name = "TestComponent"
        RecompositionTracker.track(name)
        RecompositionTracker.track(name)
        RecompositionTracker.track(name)

        assertEquals(3,RecompositionTracker.getCount(name))
    }
    @Test
    fun `reset clear all tracking data`(){
        RecompositionTracker.track("A")
        RecompositionTracker.flush()

        RecompositionTracker.reset()

        assertEquals(0,RecompositionTracker.getCount("A"))
        assertTrue(RecompositionTracker.data.isEmpty())
    }
    @Test
    fun `flush moves data to SnapshotStateMap for UI`(){
        RecompositionTracker.track("A")
        RecompositionTracker.track("A")

        RecompositionTracker.flush()

        val uiEntry = RecompositionTracker.data["A"]
        assertEquals(2,uiEntry?.count)
    }

}