package com.deutschdreamers.wordimpostor.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.deutschdreamers.wordimpostor.data.model.Difficulty
import com.deutschdreamers.wordimpostor.data.model.GameSettings
import com.deutschdreamers.wordimpostor.data.model.ThemeMode
import com.deutschdreamers.wordimpostor.data.model.TieVoteBehavior
import kotlinx.coroutines.flow.Flow
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
            )
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
}

