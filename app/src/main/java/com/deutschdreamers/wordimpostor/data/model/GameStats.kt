package com.deutschdreamers.wordimpostor.data.model

import kotlinx.serialization.Serializable

/**
 * Cumulative local play statistics. Pure/serializable so the counting logic can be
 * unit-tested and the whole thing persisted as-is.
 */
@Serializable
data class GameStats(
    val gamesPlayed: Int = 0,
    val civilianWins: Int = 0,
    val impostorWins: Int = 0
) {
    /** Returns a new copy with one more finished game recorded for [winner]. */
    fun recordedWith(winner: Winner): GameStats = when (winner) {
        Winner.CIVILIANS -> copy(
            gamesPlayed = gamesPlayed + 1,
            civilianWins = civilianWins + 1
        )

        Winner.IMPOSTORS -> copy(
            gamesPlayed = gamesPlayed + 1,
            impostorWins = impostorWins + 1
        )
    }

    /** Civilian win share in the range 0f..1f (0 when no games played yet). */
    val civilianWinRate: Float
        get() = if (gamesPlayed == 0) 0f else civilianWins.toFloat() / gamesPlayed
}

/** One player's outcome, captured for the last-game recap. */
@Serializable
data class PlayerSummary(
    val name: String,
    val role: Role,
    val wasEliminated: Boolean
)

/** A snapshot of the most recently finished game, shown on the Stats screen. */
@Serializable
data class GameRecap(
    val winner: Winner,
    val secretWord: String,
    val categoryLabel: String,
    val players: List<PlayerSummary>,
    val rounds: Int
)
