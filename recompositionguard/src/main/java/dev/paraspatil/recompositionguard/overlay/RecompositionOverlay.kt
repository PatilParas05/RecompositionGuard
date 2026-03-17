package dev.paraspatil.recompositionguard.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.paraspatil.recompositionguard.RecompositionGuard
import dev.paraspatil.recompositionguard.RecompositionTracker


@Composable
fun RecompositionOverlay(timestamp: Long) {
    if (!RecompositionGuard.isInstalled() || !RecompositionGuard.config.overlayEnabled) return

    val sortedEntries by remember(timestamp) {
        derivedStateOf {
            RecompositionTracker.data.values
                .toList() // Snapshot current map state
                .sortedByDescending { it.count }
        }
    }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .background(Color(0xEE1A1A1A), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Text(
            text = "🔍 RecompositionGuard",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        if (sortedEntries.isEmpty()) {
            Text(
                text = "Nothing tracked yet",
                color = Color(0xFF888888),
                fontSize = 10.sp
            )
        } else {
            LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                items(sortedEntries, key = { it.name }) { entry ->
                    val config = RecompositionGuard.config
                    val color = when {
                        entry.count >= config.errorThreshold -> Color(0xFFFF4444)
                        entry.count >= config.warnThreshold  -> Color(0xFFFFAA00)
                        else                                 -> Color(0xFF44DD44)
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