package com.deutschdreamers.wordimpostor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val players: List<Player> = emptyList(),
    val secretWord: String = "",
    val currentPhase: GamePhase = GamePhase.NotStarted,
    val settings: GameSettings = GameSettings(),
    val startingPlayerId: Int? = null,
    val roundHistory: List<RoundHistory> = emptyList()
)

@Serializable
data class RoundHistory(
    val roundNumber: Int,
    val clues: Map<Int, String>, // playerId -> clue
    val votes: Map<Int, Int>, // voterId -> votedForPlayerId
    val eliminatedPlayerId: Int?
)

