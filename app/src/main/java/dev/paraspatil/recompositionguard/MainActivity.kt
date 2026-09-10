package dev.paraspatil.recompositionguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.paraspatil.recompositionguard.ui.theme.RecompositionGuardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        RecompositionGuard.install(
            ThresholdConfig(
                warnThreshold = 8,
                errorThreshold = 15,
                overlayEnabled = true,
                logsEnabled = true,
                dashboardEnabled = true
            )
        )

        setContent {
            RecompositionGuardTheme {
                RapidTestScreen()
            }
        }
    }
}