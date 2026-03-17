package dev.paraspatil.recompositionguard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.paraspatil.recompositionguard.compose.RecompositionDashboard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TickViewModel : ViewModel() {
    private val _ticks = MutableStateFlow(0)
    val ticks: StateFlow<Int> = _ticks

    fun increment() {
        _ticks.value++
    }
}

@Composable
fun TestScreen(vm: TickViewModel = viewModel()) {
    val ticks by vm.ticks.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            vm.increment()
        }
    }

    Box(Modifier.padding(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("Ticks : $ticks")

            Button(onClick = { vm.increment() }) {
                Text("Tap to force recompose (+1)")
            }
            HotComposable(UnstableInt(ticks))
            ColdComposable()
        }
        RecompositionDashboard()
    }
}

class UnstableInt(val value: Int)

@Composable
fun HotComposable(ticks: UnstableInt) {
    LocalTrackRecomposition("HotComposable")
    Text(text = "I will recompose a lot :${ticks.value}")
}

@Composable
fun ColdComposable() {
    LocalTrackRecomposition("ColdComposable")
    Text(text = "I will not recompose")
}

@Composable
private fun LocalTrackRecomposition(name: String) {
    if (!RecompositionGuard.isInstalled()) return
    SideEffect {
        android.util.Log.d("TRACK_TEST", ">>> track() called: $name")
        RecompositionTracker.track(name)
    }
}