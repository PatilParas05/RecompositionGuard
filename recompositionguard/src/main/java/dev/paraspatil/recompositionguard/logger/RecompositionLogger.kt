package dev.paraspatil.recompositionguard.logger

import android.util.Log
import dev.paraspatil.recompositionguard.RecompositionGuard
import dev.paraspatil.recompositionguard.ThresholdConfig

object RecompositionLogger{
    private const val TAG ="RecompositionGuard"
    val logger = RecompositionGuard.logger

    fun log(name: String, count: Int, config: ThresholdConfig): Boolean {
        val shouldLog = when {
            count == 1 -> true
            count == config.warnThreshold -> true
            count == config.errorThreshold -> true
            count > config.errorThreshold && count % 10 == 0 -> true
            else -> false
        }

        if (!shouldLog) return false

            when {
                count >= config.errorThreshold -> {
                    logger.e(TAG, buildMessage(name, count, "🔴 EXCESSIVE"))
                    StabilityAdvisor.suggest(name, count)
                }
                count >= config.warnThreshold -> {
                    logger.w(TAG, buildMessage(name, count, "🟡 MODERATE"))
                }
                else -> {
                    logger.d(TAG, buildMessage(name, count, "🟢 OK"))
                }
            }
        return true
    }
    private fun buildMessage(name: String,count: Int,level: String): String{
        return "[$level] Composable: \"$name\" recomposed $count times(s)"
    }
}