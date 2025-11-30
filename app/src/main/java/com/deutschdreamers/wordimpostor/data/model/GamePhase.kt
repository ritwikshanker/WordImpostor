package com.deutschdreamers.wordimpostor.data.model

sealed class GamePhase {
    data object NotStarted : GamePhase()
    data class RoleReveal(val currentPlayerIndex: Int) : GamePhase()
    data class ClueRound(val currentPlayerIndex: Int, val remainingTime: Int? = null) : GamePhase()
    data object Discussion : GamePhase()
    data class Voting(val votes: Map<Int, Int> = emptyMap()) : GamePhase()
    data class EliminationReveal(val eliminatedPlayerId: Int) : GamePhase()
    data class GameEnd(val winner: Winner) : GamePhase()
}

enum class Winner {
    CIVILIANS,
    IMPOSTORS
}

