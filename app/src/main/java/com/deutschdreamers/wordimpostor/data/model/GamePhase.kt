package com.deutschdreamers.wordimpostor.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed class GamePhase {
    @Serializable
    data object NotStarted : GamePhase()

    @Serializable
    data class RoleReveal(val currentPlayerIndex: Int) : GamePhase()

    @Serializable
    data class ClueRound(val currentPlayerIndex: Int, val remainingTime: Int? = null) : GamePhase()

    @Serializable
    data object Discussion : GamePhase()

    @Serializable
    data class Voting(val votes: Map<Int, Int> = emptyMap()) : GamePhase()

    @Serializable
    data class EliminationReveal(val eliminatedPlayerId: Int) : GamePhase()

    @Serializable
    data class GameEnd(val winner: Winner) : GamePhase()
}

@Serializable
enum class Winner {
    CIVILIANS,
    IMPOSTORS
}

