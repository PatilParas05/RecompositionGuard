package dev.paraspatil.recompositionguard

import dev.paraspatil.recompositionguard.logger.RecompositionLogger
import org.junit.Assert
import org.junit.Test

class RecompositionLoggerTest {
    private  val config = ThresholdConfig(warnThreshold = 5, errorThreshold = 10)
    
    @Test
    fun `logger milestones work correctly`(){

        Assert.assertTrue(RecompositionLogger.log("Test", 1, config))

        // Should NOT log at 2 (stays silent)
        Assert.assertFalse(RecompositionLogger.log("Test", 2, config))

        // Should log at warn threshold (5)
        Assert.assertTrue(RecompositionLogger.log("Test", 5, config))

        // Should NOT log at 9
        Assert.assertFalse(RecompositionLogger.log("Test", 9, config))

        // Should log at error threshold (10)
        Assert.assertTrue(RecompositionLogger.log("Test", 10, config))

        // Should log at every 10 after error (e.g., 20)
        Assert.assertTrue(RecompositionLogger.log("Test", 20, config))

        // Should NOT log at 25
        Assert.assertFalse(RecompositionLogger.log("Test", 25, config))

    }
}