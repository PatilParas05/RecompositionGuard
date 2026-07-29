package dev.paraspatil.recompositionguard

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.staticCompositionLocalOf
import dev.paraspatil.recompositionguard.logger.RecompositionLogger
import java.util.concurrent.ConcurrentHashMap

object RecompositionTracker {

    private val rawCounts = ConcurrentHashMap<String, Int>()
    private val firstSeen = ConcurrentHashMap<String, Long>()
    val data: SnapshotStateMap<String, RecompositionData> = mutableStateMapOf()

    internal lateinit var config: ThresholdConfig
    val logger = RecompositionGuard.logger

    fun track(name: String) {
        val newCount = rawCounts.merge(name,1){old,value -> old+value}?:1

        firstSeen.putIfAbsent(name, System.currentTimeMillis())

        if (::config.isInitialized && config.logsEnabled) {
            RecompositionLogger.log(name, newCount, config)
        }
    }
    fun flush() {
        val currentRaw = HashMap(rawCounts)

            logger.d("RecompositionGuard", "Flush called at ${System.currentTimeMillis()}")

        currentRaw.forEach { (name, count) ->
            val existing = data[name]
            if (existing == null || existing.count != count) {
               data[name] = RecompositionData(
                    name = name,
                    count = count,
                    firstSeenAt = firstSeen[name] ?: System.currentTimeMillis(),
                    lastSeenAt = System.currentTimeMillis()
                )
            }
        }
    }

    fun reset() {
        rawCounts.clear()
        firstSeen.clear()
        data.clear()
    }

    fun getCount(name: String): Int = rawCounts[name] ?: 0
}

val LocalRecompositionTracker = staticCompositionLocalOf { RecompositionTracker }