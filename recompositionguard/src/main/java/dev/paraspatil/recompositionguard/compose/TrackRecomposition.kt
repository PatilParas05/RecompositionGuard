package dev.paraspatil.recompositionguard.compose


import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import dev.paraspatil.recompositionguard.RecompositionGuard
import dev.paraspatil.recompositionguard.RecompositionTracker
import androidx.compose.ui.graphics.Color

fun Modifier.trackRecomposition(name: String): Modifier =composed{
    if (!RecompositionGuard.isInstalled()) return@composed this

    SideEffect {
        RecompositionTracker.track(name)
    }
    val count = RecompositionTracker.getCount(name)
    val config = RecompositionGuard.config

    val borderColor = when{
        count >= config.errorThreshold -> Color(0xFFFF4444)
        count >= config.warnThreshold -> Color(0xFFFFAA00)
        else -> Color(0xFF44BB44)
    }
    if (config.overlayEnabled){
       this.then(
           Modifier.drawBehind {
               drawRect(
                   color = borderColor.copy(alpha = 0.5f),
               )
           }
       )
    }else this
}


@Composable
fun TrackRecomposition(name: String)  {
    if (!RecompositionGuard.isInstalled()) return
    SideEffect {
        RecompositionTracker.track(name)
    }
}