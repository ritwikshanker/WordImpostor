package com.deutschdreamers.wordimpostor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: Int,
    val name: String,
    val role: Role,
    var isEliminated: Boolean = false,
    var clue: String = ""
)


