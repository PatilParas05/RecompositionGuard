package dev.paraspatil.recompositionguard.logger

import android.util.Log
import dev.paraspatil.recompositionguard.ThresholdConfig

object RecompositionLogger{
    private const val TAG ="RecompositionGuard"

    fun log(name: String, count: Int, config: ThresholdConfig) {
    when{
        count >= config.errorThreshold -> {
            Log.e(TAG, buildMessage(name,count,"\uD83D\uDD34 EXCESSIVE"))
            StabilityAdvisor.suggest(name,count)
        }
        count >= config.warnThreshold -> {
            Log.w(TAG, buildMessage(name,count,"\uD83D\uDFE1 MODERATE"))
        }
        else ->{
            Log.d(TAG, buildMessage(name,count,"\uD83D\uDFE2 OK"))
        }
    }
    }
    private fun buildMessage(name: String,count: Int,level: String): String{
        return "[$level] Composable: \"$name\" recomposed $count times(s)"
    }
}