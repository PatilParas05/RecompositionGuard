package dev.paraspatil.recompositionguard.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import dev.paraspatil.recompositionguard.LocalRecompositionTracker
import dev.paraspatil.recompositionguard.RecompositionGuard
import kotlinx.coroutines.launch

@Composable
fun Modifier.trackRecomposition(name: String): Modifier = composed {
    val tracker = LocalRecompositionTracker.current
    val config = RecompositionGuard.config

    if (!RecompositionGuard.isInstalled()) return@composed this

    SideEffect {
        tracker.track(name)
    }

    if (config.visualFlashEnabled) {
        val scope = rememberCoroutineScope()
        val flashAlpha = remember { Animatable(0f) }

        SideEffect {
            scope.launch {
                flashAlpha.snapTo(1f)
                flashAlpha.animateTo(0f, animationSpec = tween(300))
            }
        }

        this.drawBehind {
            val alpha = flashAlpha.value
            if (alpha > 0f) {
                drawRect(color = Color.Red.copy(alpha = alpha * 0.3f))
            }
        }
    } else {
        this
    }
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
