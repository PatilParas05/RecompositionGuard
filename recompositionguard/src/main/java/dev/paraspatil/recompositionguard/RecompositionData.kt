package dev.paraspatil.recompositionguard

data class RecompositionData (
    val name : String,
    val count : Int =1,
    val firstSeenAt : Long = System.currentTimeMillis(),
    val lastSeenAt : Long = System.currentTimeMillis(),
)