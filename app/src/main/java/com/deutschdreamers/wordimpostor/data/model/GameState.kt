package com.deutschdreamers.wordimpostor.data.model

data class GameState(
    val players: List<Player> = emptyList(),
    val secretWord: String = "",
    val currentPhase: GamePhase = GamePhase.NotStarted,
    val settings: GameSettings = GameSettings(),
    val startingPlayerId: Int? = null,
    val roundHistory: List<RoundHistory> = emptyList()
)

data class RoundHistory(
    val roundNumber: Int,
    val clues: Map<Int, String>, // playerId -> clue
    val votes: Map<Int, Int>, // voterId -> votedForPlayerId
    val eliminatedPlayerId: Int?
)

