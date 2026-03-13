package dev.paraspatil.recompositionguard

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.staticCompositionLocalOf
import dev.paraspatil.recompositionguard.logger.RecompositionLogger

object RecompositionTracker{
    val data: SnapshotStateMap<String, RecompositionData> = mutableStateMapOf()
    internal lateinit var config: ThresholdConfig

    var updateTrigger = mutableStateOf(0)
        private set

    fun track(name: String){
        val existing = data[name]
        data[name]= if (existing != null){
            existing.copy(
                count = existing.count+1,
                lastSeenAt = System.currentTimeMillis()
            )
        }else{
            RecompositionData(name)
        }
        updateTrigger.value++

        if(config.logsEnabled){
            RecompositionLogger.log(name,data[name]!!.count,config)
        }
    }
    fun reset() {
        data.clear()
        updateTrigger.value = 0
    }
    fun getCount(name: String): Int= data[name]?.count?:0
}
val LocalRecompositionTracker = staticCompositionLocalOf{RecompositionTracker}