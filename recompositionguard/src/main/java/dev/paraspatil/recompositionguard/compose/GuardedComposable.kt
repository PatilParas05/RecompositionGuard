package dev.paraspatil.recompositionguard.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
@Composable
fun GuardedComposable(
    name: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.trackRecomposition(name)) {
        TrackRecomposition(name= name)
        content()
    }
}