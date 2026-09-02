package dev.paraspatil.recompositionguard.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.paraspatil.recompositionguard.RecompositionGuard
import dev.paraspatil.recompositionguard.RecompositionTracker
import kotlin.math.roundToInt


@Composable
fun RecompositionOverlay(timestamp: Long) {
    if (!RecompositionGuard.isInstalled() || !RecompositionGuard.config.overlayEnabled) return

    var isExpanded by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val sortedEntries by remember {
        derivedStateOf {
            RecompositionTracker.data.values
                .toList() // Snapshot current map state
                .sortedByDescending { it.count }
        }
    }

    val errorCount by remember {
        derivedStateOf {
            sortedEntries.count { it.count >= RecompositionGuard.config.errorThreshold }
        }
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ){
        if (!isExpanded) {
            // --- MINIMIZED BUBBLE ---
            BadgedBox(
                badge = {
                    if (errorCount > 0) {
                        Badge(containerColor = Color.Red) {
                            Text(errorCount.toString(), color = Color.White)
                        }
                    }
                },
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xEE1A1A1A))
                    .clickable { isExpanded = true },
                content = {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("🔍", fontSize = 20.sp)
                    }
                }
            )
        } else {
            // --- EXPANDED DASHBOARD ---
            Column(
                modifier = Modifier
                    .widthIn(220.dp) // Use widthIn for better flexibility
                    .wrapContentHeight()
                    .background(Color(0xEE1A1A1A), RoundedCornerShape(12.dp))
                    .padding(10.dp)
            ) {
                // HEADER ROW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "RecompositionGuard",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Pause Toggle
                        Icon(
                            imageVector = if (RecompositionTracker.isPaused()) Icons.Default.PlayArrow else Icons.Default.Refresh,
                            contentDescription = "Toggle Pause",
                            tint = Color.White,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { RecompositionTracker.togglePause() }
                        )

                        Spacer(Modifier.width(8.dp))

                        // Clear Button
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            tint = Color.White,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { RecompositionTracker.reset() }
                        )

                        Spacer(Modifier.width(8.dp))

                        // Minimize Button
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Minimize",
                            tint = Color.White,
                            modifier = Modifier
                                .size(18.dp)
                                .clickable { isExpanded = false }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // LIST CONTENT
                if (sortedEntries.isEmpty()) {
                    Text("Nothing tracked yet", color = Color.Gray, fontSize = 10.sp)
                } else {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 250.dp)
                    ) {
                        items(sortedEntries, key = { it.name }) { entry ->
                            val color = when {
                                entry.count >= RecompositionGuard.config.errorThreshold -> Color(0xFFFF4444)
                                entry.count >= RecompositionGuard.config.warnThreshold -> Color(0xFFFFAA00)
                                else -> Color(0xFF44DD44)
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    entry.name,
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "[${entry.count}x]",
                                    color = color,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}