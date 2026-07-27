package dev.paraspatil.recompositionguard.logger

import android.util.Log
import dev.paraspatil.recompositionguard.ThresholdConfig

object RecompositionLogger{
    private const val TAG ="RecompositionGuard"

    fun log(name: String, count: Int, config: ThresholdConfig): Boolean {
        val shouldLog = when {
            count == 1 -> true
            count == config.warnThreshold -> true
            count == config.errorThreshold -> true
            count > config.errorThreshold && count % 10 == 0 -> true
            else -> false
        }

        if (!shouldLog) return false

        try {
            when {
                count >= config.errorThreshold -> {
                    Log.e(TAG, buildMessage(name, count, "🔴 EXCESSIVE"))
                    StabilityAdvisor.suggest(name, count)
                }
                count >= config.warnThreshold -> {
                    Log.w(TAG, buildMessage(name, count, "🟡 MODERATE"))
                }
                else -> {
                    Log.d(TAG, buildMessage(name, count, "🟢 OK"))
                }
            }
        } catch (e: Exception) {
            // Handle cases where android.util.Log is not mocked in Unit Tests
        }
        return true
    }
    private fun buildMessage(name: String,count: Int,level: String): String{
        return "[$level] Composable: \"$name\" recomposed $count times(s)"
    }
}