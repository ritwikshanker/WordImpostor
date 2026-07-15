package com.deutschdreamers.wordimpostor.data.model

data class GameSettings(
    val timerEnabled: Boolean = false,
    val timerDuration: Int = 30, // seconds
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val wordCategory: WordCategory = WordCategory.MIXED,
    // When on, impostors get a subtle hint (the category, or the difficulty band).
    val impostorHintEnabled: Boolean = false,
    val allowSelfVoting: Boolean = false,
    val tieVoteBehavior: TieVoteBehavior = TieVoteBehavior.NO_ELIMINATION,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    // Material You dynamic color is opt-in; the brand palette is the default look.
    val dynamicColor: Boolean = false,
    // Tactile + audible feedback (honours device-level touch sound/vibration too).
    val soundHapticsEnabled: Boolean = true
)

enum class TieVoteBehavior {
    NO_ELIMINATION,
    RANDOM_ELIMINATION,
    REVOTE
}

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

