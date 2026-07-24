package dev.paraspatil.recompositionguard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import dev.paraspatil.recompositionguard.LocalRecompositionTracker
import dev.paraspatil.recompositionguard.RecompositionGuard
import dev.paraspatil.recompositionguard.RecompositionTracker


@Composable
fun Modifier.trackRecomposition(name: String): Modifier = composed {
    val tracker = LocalRecompositionTracker.current
    if (RecompositionGuard.isInstalled()) {
        SideEffect {
            tracker.track(name)
        }
    }
    this
}
@Composable
fun TrackRecomposition(name: String, key: Any? = Unit) {
    if (!RecompositionGuard.isInstalled()) return
    val tracker = LocalRecompositionTracker.current
    androidx.compose.runtime.remember(key) { true }
    SideEffect {
        tracker.track(name)
    }
}