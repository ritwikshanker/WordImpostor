package com.deutschdreamers.wordimpostor.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.deutschdreamers.wordimpostor.data.model.GameRecap
import com.deutschdreamers.wordimpostor.data.model.GameStats
import com.deutschdreamers.wordimpostor.data.model.Winner
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.statsDataStore: DataStore<Preferences> by preferencesDataStore(name = "stats")

/**
 * Persists cumulative [GameStats] and the most recent [GameRecap] locally. Stats are kept
 * as plain counters; the recap is stored as JSON via kotlinx.serialization.
 */
class StatsRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    private object Keys {
        val GAMES_PLAYED = intPreferencesKey("games_played")
        val CIVILIAN_WINS = intPreferencesKey("civilian_wins")
        val IMPOSTOR_WINS = intPreferencesKey("impostor_wins")
        val LAST_RECAP_JSON = stringPreferencesKey("last_recap_json")
    }

    val statsFlow: Flow<GameStats> = context.statsDataStore.data.map { prefs ->
        GameStats(
            gamesPlayed = prefs[Keys.GAMES_PLAYED] ?: 0,
            civilianWins = prefs[Keys.CIVILIAN_WINS] ?: 0,
            impostorWins = prefs[Keys.IMPOSTOR_WINS] ?: 0
        )
    }

    val lastRecapFlow: Flow<GameRecap?> = context.statsDataStore.data.map { prefs ->
        prefs[Keys.LAST_RECAP_JSON]?.let { raw ->
            runCatching { json.decodeFromString<GameRecap>(raw) }.getOrNull()
        }
    }

    /** Records a finished game: bumps the win counters and stores its recap. */
    suspend fun recordGame(recap: GameRecap) {
        context.statsDataStore.edit { prefs ->
            prefs[Keys.GAMES_PLAYED] = (prefs[Keys.GAMES_PLAYED] ?: 0) + 1
            when (recap.winner) {
                Winner.CIVILIANS ->
                    prefs[Keys.CIVILIAN_WINS] = (prefs[Keys.CIVILIAN_WINS] ?: 0) + 1

                Winner.IMPOSTORS ->
                    prefs[Keys.IMPOSTOR_WINS] = (prefs[Keys.IMPOSTOR_WINS] ?: 0) + 1
            }
            prefs[Keys.LAST_RECAP_JSON] = json.encodeToString(recap)
        }
    }

    /** Clears all stats and the stored recap. */
    suspend fun resetStats() {
        context.statsDataStore.edit { it.clear() }
    }
}
