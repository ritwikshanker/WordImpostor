package com.deutschdreamers.wordimpostor

import com.deutschdreamers.wordimpostor.data.model.Difficulty
import com.deutschdreamers.wordimpostor.data.model.GamePhase
import com.deutschdreamers.wordimpostor.data.model.GameSettings
import com.deutschdreamers.wordimpostor.data.model.GameState
import com.deutschdreamers.wordimpostor.data.model.Player
import com.deutschdreamers.wordimpostor.data.model.Role
import com.deutschdreamers.wordimpostor.data.model.RoundHistory
import com.deutschdreamers.wordimpostor.data.model.WordCategory
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Guards the process-death persistence contract: an in-progress [GameState] must round-trip
 * through JSON unchanged, including the sealed [GamePhase] hierarchy and the settings/history.
 */
class GameStatePersistenceTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun gameState_roundTripsThroughJson() {
        val original = GameState(
            players = listOf(
                Player(id = 0, name = "Ada", role = Role.CIVILIAN, clue = "code"),
                Player(id = 1, name = "Bob", role = Role.IMPOSTOR, isEliminated = true)
            ),
            secretWord = "Algorithm",
            currentPhase = GamePhase.ClueRound(currentPlayerIndex = 1, remainingTime = 17),
            settings = GameSettings(
                difficulty = Difficulty.HARD,
                wordCategory = WordCategory.SCIENCE,
                impostorHintEnabled = true
            ),
            startingPlayerId = 0,
            roundHistory = listOf(
                RoundHistory(
                    roundNumber = 1,
                    clues = mapOf(0 to "code", 1 to "—"),
                    votes = mapOf(0 to 1),
                    eliminatedPlayerId = 1
                )
            )
        )

        val restored = json.decodeFromString<GameState>(json.encodeToString(original))

        assertEquals(original, restored)
    }

    @Test
    fun notStartedPhase_roundTrips() {
        val original = GameState()
        val restored = json.decodeFromString<GameState>(json.encodeToString(original))
        assertEquals(GamePhase.NotStarted, restored.currentPhase)
        assertEquals(original, restored)
    }
}
