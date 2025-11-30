package com.deutschdreamers.wordimpostor.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object Setup : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object RoleReveal : Screen

    @Serializable
    data object ClueRound : Screen

    @Serializable
    data object Discussion : Screen

    @Serializable
    data object Voting : Screen

    @Serializable
    data object EliminationReveal : Screen

    @Serializable
    data object GameEnd : Screen

    @Serializable
    data object About : Screen
}

