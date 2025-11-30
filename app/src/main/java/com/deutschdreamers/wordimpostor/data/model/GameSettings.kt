package com.deutschdreamers.wordimpostor.data.model

data class GameSettings(
    val timerEnabled: Boolean = false,
    val timerDuration: Int = 30, // seconds
    val difficulty: Difficulty = Difficulty.MEDIUM,
    val allowSelfVoting: Boolean = false,
    val tieVoteBehavior: TieVoteBehavior = TieVoteBehavior.NO_ELIMINATION,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
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

