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
fun TestScreen(){
    var tricks by remember { mutableStateOf(0) }
    var tricks2 by remember { mutableStateOf(0) }
    val stableText = remember { "I will not recompose" }

    LaunchedEffect(Unit) {
    while (true){
        kotlinx.coroutines.delay(1000)
        tricks++
        tricks2++
        }
    }
    Box(Modifier.padding(16.dp)){
        Column(Modifier.padding(16.dp)) {
            Text("Ticks : $tricks")
            Text("Ticks2 : $tricks2")
            GuardedComposable(name = "HotComposable") {
                Text("I will recompose a lot :$tricks")
            }
            GuardedComposable(name = "ColdComposable") {
                Text(stableText)
            }
        }
        RecompositionDashboard()
    }
}