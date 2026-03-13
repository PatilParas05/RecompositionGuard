package dev.paraspatil.recompositionguard.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.traceEventStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.paraspatil.recompositionguard.RecompositionGuard
import dev.paraspatil.recompositionguard.RecompositionTracker
import kotlinx.coroutines.delay

@Composable
fun RecompositionOverlay(){
    if (!RecompositionGuard.isInstalled())return
    if (!RecompositionGuard.config.overlayEnabled) return
    val trigger by RecompositionTracker.updateTrigger

    val entries = RecompositionTracker.data.values
        .sortedByDescending { it.count }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(Color(0xCC000000), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(
            text = "🔍 RecompositionGuard",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        if (entries.isNotEmpty()) {
            Text(
                text = "Nothing tracked yet",
                color = Color(0xFF888888),
                fontSize = 10.sp
            )
        } else {
            LazyColumn(modifier = Modifier.heightIn(max = 300.dp)){
                items(entries.toList(),key = { it.name }) { entry ->
                    val config = RecompositionGuard.config
                    val color = when {
                        entry.count >= config.errorThreshold -> Color(0xFFFF4444)
                        entry.count >= config.warnThreshold -> Color(0xFFFFAA00)
                        else -> Color(0xFF44BB44)
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = entry.name,
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "[${entry.count}x]",
                            color = color,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}