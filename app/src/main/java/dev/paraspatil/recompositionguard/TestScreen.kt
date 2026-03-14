package dev.paraspatil.recompositionguard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.paraspatil.recompositionguard.compose.GuardedComposable
import dev.paraspatil.recompositionguard.compose.RecompositionDashboard

@Composable
fun TestScreen() {
    var ticks by remember { mutableStateOf(0) }
    var ticks2 by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            ticks++
            ticks2++
        }
    }

    Box(Modifier.padding(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Ticks : $ticks")
            Text("Ticks2 : $ticks2")

            // HotComposable — reads ticks, so it SHOULD recompose every tick
            GuardedComposable(name = "HotComposable") {
                Text("I will recompose a lot :$ticks")
            }

            // ColdComposable — does NOT read ticks, but still recomposes
            // because it shares the same Column scope that reads ticks above.
            // To truly prevent recomposition, extract it to a separate composable
            // so Compose can skip it when its inputs haven't changed:
            StableColdComposable()
        }
        RecompositionDashboard()
    }
}

//  Extracted to its own composable — Compose can now SKIP this entirely
// when called from a recomposing parent, because it has no unstable parameters.
@Composable
fun StableColdComposable() {
    // stableText is a compile-time constant — Compose knows it never changes
    GuardedComposable(name = "ColdComposable") {
        Text("I will not recompose")
    }
}