package dev.paraspatil.recompositionguard.logger

import android.util.Log

object StabilityAdvisor {
    private const val TAG = "RecompositionGuard"
    private val advisedComposables = mutableSetOf<String>()

    fun suggest(name: String, count: Int) {
        if (advisedComposables.contains(name)) return
        
        advisedComposables.add(name)
        try {
            Log.w(TAG,"""
                 ⚠️  [$name] recomposed $count times. Possible causes:
                -> Unstable lambda - wrap with remember { }
                -> Data class missing @Stable or @Immutable annotation
                -> State read inside composition - hoist it up
                -> Inline function triggering parent recomposition
                -> Use derivedStateOf { } for computed state
            """.trimIndent())
        } catch (e: Exception) {
            // Ignore Log crashes in JVM unit tests
        }
    }

    fun reset() {
        advisedComposables.clear()
    }
}
