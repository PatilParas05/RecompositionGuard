package dev.paraspatil.recompositionguard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.paraspatil.recompositionguard.overlay.RecompositionOverlay
import org.junit.Rule
import org.junit.Test

class RecompositionOverlayTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun overlayDisplayCorrectCounts(){
        RecompositionGuard.install(ThresholdConfig())
        RecompositionTracker.reset()
        RecompositionTracker.track("ComponentA")
        RecompositionTracker.track("ComponentA")
        RecompositionTracker.flush()

        composeTestRule.setContent {
            RecompositionOverlay(timestamp = System.currentTimeMillis())
        }
        composeTestRule.onNodeWithText("ComponentA").assertIsDisplayed()
        composeTestRule.onNodeWithText("[2x]").assertIsDisplayed()
    }

    @Test
    fun overlayShowsEmptyState(){
        RecompositionTracker.reset()
        composeTestRule.setContent {
            RecompositionOverlay(timestamp = System.currentTimeMillis())
        }
        composeTestRule.onNodeWithText("Nothing tracked yet").assertIsDisplayed()
    }
}