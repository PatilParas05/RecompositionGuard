package dev.paraspatil.recompositionguard.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import dev.paraspatil.recompositionguard.RecompositionGuard
import dev.paraspatil.recompositionguard.RecompositionTracker

@Composable
fun GuardedComposable(
    name: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (!RecompositionGuard.isInstalled()) {
        Box(modifier = modifier) { content() }
        return
    }

    SideEffect {
        RecompositionTracker.track(name)
    }

    Box(modifier = modifier) {
        content()
    }
}
