package dev.paraspatil.recompositionguard.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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

    var refresh by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit){
        while (true){
            delay(300)
            refresh++
        }
    }
    val entries = RecompositionTracker.data.values.sortedByDescending { it.count }
        .sortedByDescending { it.count }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC000000), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ){
        Text(
            text = "🔍 RecompositionGuard",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        LazyColumn {
            items(entries.toList()){entry ->
                val config = RecompositionGuard.config
                val color = when{
                    entry.count >= config.errorThreshold -> Color(0xFFFF4444)
                    entry.count >= config.warnThreshold  -> Color(0xFFFFAA00)
                    else -> Color(0xFF44BB44)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = entry.name,
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier.weight(1f)
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