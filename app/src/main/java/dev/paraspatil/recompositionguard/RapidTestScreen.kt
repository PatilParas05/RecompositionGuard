package dev.paraspatil.recompositionguard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.paraspatil.recompositionguard.compose.RecompositionDashboard
import dev.paraspatil.recompositionguard.compose.trackRecomposition
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RapidViewModel : ViewModel() {
    private val _rapidCounter = MutableStateFlow(0)
    val rapidCounter: StateFlow<Int> = _rapidCounter

    fun increment() { _rapidCounter.value++ }
}

@Composable
fun RapidTestScreen(vm: RapidViewModel = viewModel()) {
    val counter by vm.rapidCounter.collectAsStateWithLifecycle()

    // Auto-increment to simulate real-time load
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            vm.increment()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.padding(24.dp)) {
            Text("⚡ Performance Demo", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Counter: $counter", fontSize = 18.sp, color = Color.Gray)

            Spacer(Modifier.height(16.dp))

            // Section 1: The "hot" zone (Expected to recompose)
            Text("🔥 SHOULD RECOMPOSE (Dynamic)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(Modifier.padding(8.dp)) {
                    RapidHotComposable(count = counter)
                }
            }

            Spacer(Modifier.height(16.dp))

            // Section 2: The "cold" zone (Should stay at [1x])
            Text("❄️ SHOULD SKIP (Optimized)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(Modifier.padding(8.dp)) {
                    // This is skipped because strings are stable and never change
                    OptimizedStaticItem(label = "User Profile (Static)")
                    OptimizedStaticItem(label = "Settings Header (Static)")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Section 3: The "warning" zone (Unstable Lambda)
            Text("⚠️ BUGGY CODE (Unstable Lambda)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Card(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Column(Modifier.padding(8.dp)) {
                    // This recomposes every time because the lambda is not 'remembered'
                    UnstableLambdaItem(onAction = { println("Click!") })
                }
            }

            Spacer(Modifier.weight(1f))
            Button(onClick = { vm.increment() }, Modifier.fillMaxWidth()) {
                Text("Manual Increment")
            }
        }

        RecompositionDashboard(alignment = Alignment.BottomCenter)
    }
}

@Composable
fun RapidHotComposable(count: Int) {
    Text("Active Counter: $count", modifier = Modifier.trackRecomposition("HotCounter"))
}

@Composable
fun OptimizedStaticItem(label: String) {
    // This will stay at [1x] because the label never changes
    Text(label, modifier = Modifier.trackRecomposition("Static_$label"))
}

@Composable
fun UnstableLambdaItem(onAction: () -> Unit) {
    // This will recompose unnecessarily because the lambda is a new object every time
    Button(onClick = onAction, modifier = Modifier.trackRecomposition("BuggyLambdaButton")) {
        Text("Clicking this works, but I'm 'Hot'!")
    }
}