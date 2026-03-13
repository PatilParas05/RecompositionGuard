package dev.paraspatil.recompositionguard

data class RecompositionData (
    val name : String,
    var count : Int =0,
    val firstSeenAt : Long = System.currentTimeMillis(),
    var lastSeenAt : Long = System.currentTimeMillis(),
)