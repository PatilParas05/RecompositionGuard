package dev.paraspatil.recompositionguard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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

    fun incrementRapid() {
        _rapidCounter.value++
    }
}

@Composable
fun RapidTestScreen(vm: RapidViewModel = viewModel()) {
    val counter by vm.rapidCounter.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            vm.incrementRapid()
        }
    }

    Box(Modifier.padding(16.dp)) {
        Column(Modifier.padding(16.dp)) {
            Text("⚡ RAPID TEST - Counter: $counter")
            Text("Watch the [Nx] counts increase rapidly!")

            HorizontalDivider()

            RapidHotComposable(count = counter)
            RapidColdComposable(trigger = counter)
            RapidUnstableComposable(unstableValue = counter.toString())
        }
        

        RecompositionDashboard(alignment = Alignment.Center)
    }
}

@Composable
fun RapidHotComposable(count: Int) {
    Text(
        text = "🔥 Hot: $count",
        modifier = Modifier.trackRecomposition("RapidHotComposable")
    )
}

@Composable
fun RapidColdComposable(trigger: Int) {
    Text(
        text = "❄️ Cold: Static Content",
        modifier = Modifier.trackRecomposition("RapidColdComposable")
    )
}

@Composable
fun RapidUnstableComposable(unstableValue: String) {
    Text(
        text = "⚠️ Unstable: $unstableValue",
        modifier = Modifier.trackRecomposition("RapidUnstableComposable")
    )
}
