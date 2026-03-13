package dev.paraspatil.recompositionguard

object RecompositionGuard {
    private var _config = ThresholdConfig()
    val config : ThresholdConfig get() = _config
    private var _installed = false

    fun install(config: ThresholdConfig = ThresholdConfig()){
        _config = config
        RecompositionTracker.config = config
        _installed =true
    }
    fun isInstalled(): Boolean = _installed
    fun reset(){
        RecompositionTracker.reset()
    }
}