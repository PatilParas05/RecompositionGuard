package dev.paraspatil.recompositionguard.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.paraspatil.recompositionguard.RecompositionTracker
import dev.paraspatil.recompositionguard.overlay.RecompositionOverlay
import kotlinx.coroutines.delay

@Composable
fun RecompositionDashboard(
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopEnd,
    flushIntervalMs: Long = 100L
){
    var lastFlushTime by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(flushIntervalMs)
            RecompositionTracker.flush()
            lastFlushTime = System.currentTimeMillis()
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = alignment
    ){
        Box(
            modifier = Modifier.widthIn(max = 220.dp)
        ) {
            RecompositionOverlay(timestamp = lastFlushTime)
        }
    }
}
