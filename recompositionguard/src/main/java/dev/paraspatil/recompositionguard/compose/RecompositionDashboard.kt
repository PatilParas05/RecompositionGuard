package dev.paraspatil.recompositionguard.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.paraspatil.recompositionguard.overlay.RecompositionOverlay

@Composable
fun RecompositionDashboard(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopEnd
){
    Box(
        modifier = modifier.fillMaxSize().padding(8.dp),
        contentAlignment = alignment
    ){
        Box(
            modifier = Modifier.widthIn(max = 220.dp)
        ) {
            RecompositionOverlay()
        }
    }
}
