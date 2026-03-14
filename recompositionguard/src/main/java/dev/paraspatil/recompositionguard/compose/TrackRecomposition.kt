package dev.paraspatil.recompositionguard.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import dev.paraspatil.recompositionguard.RecompositionGuard
import dev.paraspatil.recompositionguard.RecompositionTracker


@Composable
fun TrackRecomposition(name: String) {
    if (!RecompositionGuard.isInstalled()) return
    SideEffect {
        RecompositionTracker.track(name)
    }
}
