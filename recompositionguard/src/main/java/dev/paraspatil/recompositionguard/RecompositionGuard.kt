package dev.paraspatil.recompositionguard

import dev.paraspatil.recompositionguard.logger.AndroidLogStrategy
import dev.paraspatil.recompositionguard.logger.LogStrategy

object RecompositionGuard {
    private var _config = ThresholdConfig()
    val config : ThresholdConfig get() = _config
    private var _installed = false

   var logger : LogStrategy = AndroidLogStrategy()

    fun install(config: ThresholdConfig = ThresholdConfig()){
        _config = config
        RecompositionTracker.config = config
        _installed =true
    }
    fun isInstalled(): Boolean = _installed
    fun reset(){
        RecompositionTracker.reset()
        dev.paraspatil.recompositionguard.logger.StabilityAdvisor.reset()
    }
}