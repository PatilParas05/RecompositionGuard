package dev.paraspatil.recompositionguard

import androidx.compose.runtime.staticCompositionLocalOf

object RecompositionTracker{
    private val _data = mutableMapOf<String, RecompositionData>()
    val data : Map<String,RecompositionData>get()=_data
    internal lateinit var config: ThresholdConfig

    fun track(name: String){
        val entry = _data.getOrPut(name){ RecompositionData(name) }
        entry.increment()

        if(config.logsEnabled){
            RecompositionLogger.log(name,entry.count,config)
        }
    }
    fun reset(){
        _data.clear()
    }
    fun getCount(name: String): Int= _data[name]?.count?:0
}
val LocalRecompositionTracker = staticCompositionLocalOf{RecompositionTracker}