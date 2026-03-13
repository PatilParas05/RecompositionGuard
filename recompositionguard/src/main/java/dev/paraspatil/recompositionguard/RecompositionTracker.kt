package dev.paraspatil.recompositionguard

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.staticCompositionLocalOf
import dev.paraspatil.recompositionguard.logger.RecompositionLogger

object RecompositionTracker{
    private val _data = mutableStateMapOf<String, RecompositionData>()
    val data : Map<String,RecompositionData>get()=_data
    internal lateinit var config: ThresholdConfig

    var updateTrigger = mutableStateOf(0)
        private set

    fun track(name: String){
        val entry = _data.getOrPut(name){ RecompositionData(name) }
        entry.increment()
        _data[name] = entry
        updateTrigger.value++

        if(config.logsEnabled){
            RecompositionLogger.log(name,entry.count,config)
        }
    }
    fun reset() {
        _data.clear()
        updateTrigger.value = 0
    }
    fun getCount(name: String): Int= _data[name]?.count?:0
}
val LocalRecompositionTracker = staticCompositionLocalOf{RecompositionTracker}