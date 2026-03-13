package dev.paraspatil.recompositionguard

data class ThresholdConfig(
    val warnThreshold: Int=5,
    val errorThreshold: Int=10,
    val overlayEnabled: Boolean=true,
    val logsEnabled: Boolean=true,
    val dashboardEnabled: Boolean=true
)