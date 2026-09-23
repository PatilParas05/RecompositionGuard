package dev.paraspatil.recompositionguard

import android.content.Context
import androidx.startup.Initializer

class RecompositionGuardInitializer : Initializer<Unit>{
    override fun create(context: Context){
        if(!RecompositionGuard.isInstalled()){
            RecompositionGuard.install(ThresholdConfig())
        }
    }
    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
        }

}