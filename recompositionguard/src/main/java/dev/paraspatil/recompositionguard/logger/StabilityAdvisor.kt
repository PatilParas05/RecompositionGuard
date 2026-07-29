package dev.paraspatil.recompositionguard.logger

import android.util.Log
import dev.paraspatil.recompositionguard.RecompositionGuard

object StabilityAdvisor {
    private const val TAG = "RecompositionGuard"
    private val advisedComposables = mutableSetOf<String>()
    val logger = RecompositionGuard.logger
    fun suggest(name: String, count: Int) {
        if (advisedComposables.contains(name)) return
        
        advisedComposables.add(name)

            logger.w(TAG,"""
                 ⚠️  [$name] recomposed $count times. Possible causes:
                -> Unstable lambda - wrap with remember { }
                -> Data class missing @Stable or @Immutable annotation
                -> State read inside composition - hoist it up
                -> Inline function triggering parent recomposition
                -> Use derivedStateOf { } for computed state
            """.trimIndent())
    }

    fun reset() {
        advisedComposables.clear()
    }
}
