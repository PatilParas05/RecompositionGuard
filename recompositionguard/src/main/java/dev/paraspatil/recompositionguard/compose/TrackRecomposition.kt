package dev.paraspatil.recompositionguard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import dev.paraspatil.recompositionguard.RecompositionGuard
import dev.paraspatil.recompositionguard.RecompositionTracker


@Composable
fun Modifier.trackRecomposition(name: String): Modifier = composed {
    if (RecompositionGuard.isInstalled()) {
        SideEffect {
            RecompositionTracker.track(name)
        }
    }
    this
}
@Composable
fun TrackRecomposition(name: String, key: Any? = Unit) {
    if (!RecompositionGuard.isInstalled()) return
    val _readKey = key // Force a read of the state
    SideEffect {
        RecompositionTracker.track(name)
    }
}