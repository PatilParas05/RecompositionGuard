package dev.paraspatil.recompositionguard

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.staticCompositionLocalOf
import dev.paraspatil.recompositionguard.logger.RecompositionLogger

object RecompositionTracker {

    private val rawCounts = HashMap<String, Int>()
    private val firstSeen = HashMap<String, Long>()
    val data: SnapshotStateMap<String, RecompositionData> = mutableStateMapOf()

    internal lateinit var config: ThresholdConfig

    fun track(name: String) {
        val newCount = (rawCounts[name] ?: 0) + 1
        rawCounts[name] = newCount
        if (!firstSeen.containsKey(name)) firstSeen[name] = System.currentTimeMillis()

        if (config.logsEnabled) {
            RecompositionLogger.log(name, newCount, config)
        }
    }
    fun flush() {
        for ((name, count) in rawCounts) {
            val existing = data[name]
            if (existing == null || existing.count != count) {
                data[name] = RecompositionData(
                    name        = name,
                    count       = count,
                    firstSeenAt = firstSeen[name] ?: System.currentTimeMillis(),
                    lastSeenAt  = System.currentTimeMillis()
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