package com.deutschdreamers.wordimpostor.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.deutschdreamers.wordimpostor.data.model.Difficulty
import com.deutschdreamers.wordimpostor.data.model.GameSettings
import com.deutschdreamers.wordimpostor.data.model.ThemeMode
import com.deutschdreamers.wordimpostor.data.model.TieVoteBehavior
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private object PreferencesKeys {
        val TIMER_ENABLED = booleanPreferencesKey("timer_enabled")
        val TIMER_DURATION = intPreferencesKey("timer_duration")
        val DIFFICULTY = stringPreferencesKey("difficulty")
        val ALLOW_SELF_VOTING = booleanPreferencesKey("allow_self_voting")
        val TIE_VOTE_BEHAVIOR = stringPreferencesKey("tie_vote_behavior")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val SOUND_HAPTICS_ENABLED = booleanPreferencesKey("sound_haptics_enabled")

        // In-app review tracking (app-level, not part of game settings).
        val GAMES_COMPLETED = intPreferencesKey("games_completed")
        val LAST_REVIEW_EPOCH_DAY = longPreferencesKey("last_review_epoch_day")
    }

    val settingsFlow: Flow<GameSettings> = context.dataStore.data.map { preferences ->
        GameSettings(
            timerEnabled = preferences[PreferencesKeys.TIMER_ENABLED] ?: false,
            timerDuration = preferences[PreferencesKeys.TIMER_DURATION] ?: 30,
            difficulty = Difficulty.valueOf(
                preferences[PreferencesKeys.DIFFICULTY] ?: Difficulty.MEDIUM.name
            ),
            allowSelfVoting = preferences[PreferencesKeys.ALLOW_SELF_VOTING] ?: false,
            tieVoteBehavior = TieVoteBehavior.valueOf(
                preferences[PreferencesKeys.TIE_VOTE_BEHAVIOR] ?: TieVoteBehavior.NO_ELIMINATION.name
            ),
            themeMode = ThemeMode.valueOf(
                preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
            ),
            dynamicColor = preferences[PreferencesKeys.DYNAMIC_COLOR] ?: false,
            soundHapticsEnabled = preferences[PreferencesKeys.SOUND_HAPTICS_ENABLED] ?: true
        )
    }

    suspend fun updateSettings(settings: GameSettings) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIMER_ENABLED] = settings.timerEnabled
            preferences[PreferencesKeys.TIMER_DURATION] = settings.timerDuration
            preferences[PreferencesKeys.DIFFICULTY] = settings.difficulty.name
            preferences[PreferencesKeys.ALLOW_SELF_VOTING] = settings.allowSelfVoting
            preferences[PreferencesKeys.TIE_VOTE_BEHAVIOR] = settings.tieVoteBehavior.name
            preferences[PreferencesKeys.THEME_MODE] = settings.themeMode.name
            preferences[PreferencesKeys.DYNAMIC_COLOR] = settings.dynamicColor
            preferences[PreferencesKeys.SOUND_HAPTICS_ENABLED] = settings.soundHapticsEnabled
        }
    }

    suspend fun updateTimerEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIMER_ENABLED] = enabled
        }
    }

    suspend fun updateTimerDuration(duration: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIMER_DURATION] = duration
        }
    }

    suspend fun updateDifficulty(difficulty: Difficulty) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DIFFICULTY] = difficulty.name
        }
    }

    suspend fun updateAllowSelfVoting(allow: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ALLOW_SELF_VOTING] = allow
        }
    }

    suspend fun updateTieVoteBehavior(behavior: TieVoteBehavior) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIE_VOTE_BEHAVIOR] = behavior.name
        }
    }

    suspend fun updateDynamicColor(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DYNAMIC_COLOR] = enabled
        }
    }

    // ── In-app review tracking ────────────────────────────────────────────────

    /** Number of games the player has completed (used to gate the review prompt). */
    suspend fun getGamesCompleted(): Int =
        context.dataStore.data.first()[PreferencesKeys.GAMES_COMPLETED] ?: 0

    /** Epoch day of the last time the in-app review flow was requested (0 = never). */
    suspend fun getLastReviewEpochDay(): Long =
        context.dataStore.data.first()[PreferencesKeys.LAST_REVIEW_EPOCH_DAY] ?: 0L

    /** Increment the completed-games counter and return the new total. */
    suspend fun incrementGamesCompleted(): Int {
        var updated = 0
        context.dataStore.edit { preferences ->
            updated = (preferences[PreferencesKeys.GAMES_COMPLETED] ?: 0) + 1
            preferences[PreferencesKeys.GAMES_COMPLETED] = updated
        }
        return updated
    }

    /** Record that the review flow was requested on the given epoch day. */
    suspend fun recordReviewRequested(epochDay: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_REVIEW_EPOCH_DAY] = epochDay
        }
    }
}

