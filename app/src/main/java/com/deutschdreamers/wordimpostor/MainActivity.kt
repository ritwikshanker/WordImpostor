package com.deutschdreamers.wordimpostor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deutschdreamers.wordimpostor.data.model.*
import com.deutschdreamers.wordimpostor.data.repository.SettingsRepository
import com.deutschdreamers.wordimpostor.data.repository.WordRepository
import com.deutschdreamers.wordimpostor.ui.navigation.Screen
import com.deutschdreamers.wordimpostor.ui.screens.*
import com.deutschdreamers.wordimpostor.ui.theme.WordImpostorTheme
import com.deutschdreamers.wordimpostor.ui.viewmodel.GameViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordImpostorTheme {
                val colorScheme = MaterialTheme.colorScheme

                // Set system bar colors to match theme
                SideEffect {
                    val statusBarColor = colorScheme.surface.toArgb()
                    val navigationBarColor = colorScheme.surface.toArgb()

                    @Suppress("DEPRECATION")
                    window.statusBarColor = statusBarColor
                    @Suppress("DEPRECATION")
                    window.navigationBarColor = navigationBarColor

                    // Determine if we need light or dark icons based on background luminance
                    // If surface is light (high luminance), use dark icons
                    // If surface is dark (low luminance), use light icons
                    val isLight = colorScheme.surface.luminance() > 0.5f

                    val insetsController =
                        WindowCompat.getInsetsController(window, window.decorView)
                    insetsController.isAppearanceLightStatusBars = isLight
                    insetsController.isAppearanceLightNavigationBars = isLight
                }

                WordImpostorApp()
            }
        }
    }
}

@Composable
fun WordImpostorApp() {
    val navController = rememberNavController()
    val wordRepository = remember { WordRepository() }
    val settingsRepository = remember { SettingsRepository(navController.context) }

    val gameViewModel: GameViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GameViewModel(wordRepository, settingsRepository) as T
            }
        }
    )

    val gameState by gameViewModel.gameState.collectAsState()
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNewGame = { navController.navigate(Screen.Setup) },
                onSettings = { navController.navigate(Screen.Settings) },
                onAbout = { navController.navigate(Screen.About) }
            )
        }

        composable<Screen.Setup> {
            SetupScreen(
                difficulty = gameState.settings.difficulty,
                onBack = { navController.popBackStack() },
                onStartGame = { playerNames, impostorCount ->
                    gameViewModel.startGame(playerNames, impostorCount)
                    navController.navigate(Screen.RoleReveal) {
                        popUpTo(Screen.Home)
                    }
                }
            )
        }

        composable<Screen.Settings> {
            SettingsScreen(
                settings = gameState.settings,
                onBack = { navController.popBackStack() },
                onUpdateSettings = { settings ->
                    scope.launch {
                        settingsRepository.updateSettings(settings)
                    }
                }
            )
        }

        composable<Screen.RoleReveal> {
            val currentPhase = gameState.currentPhase
            if (currentPhase is GamePhase.RoleReveal) {
                val currentPlayer = gameState.players.getOrNull(currentPhase.currentPlayerIndex)
                if (currentPlayer != null) {
                    RoleRevealScreen(
                        currentPlayer = currentPlayer,
                        secretWord = gameState.secretWord,
                        onContinue = {
                            gameViewModel.revealNextRole()
                        }
                    )
                }
            } else {
                // All roles revealed, move to clue round
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.ClueRound) {
                        popUpTo(Screen.Home)
                    }
                }
            }
        }

        composable<Screen.ClueRound> {
            val currentPhase = gameState.currentPhase
            if (currentPhase is GamePhase.ClueRound) {
                val currentPlayer = gameState.players.getOrNull(currentPhase.currentPlayerIndex)
                if (currentPlayer != null) {
                    ClueRoundScreen(
                        currentPlayer = currentPlayer,
                        secretWord = gameState.secretWord,
                        remainingTime = currentPhase.remainingTime,
                        onSubmitClue = { clue ->
                            gameViewModel.submitClue(clue)
                        }
                    )
                }
            } else if (currentPhase is GamePhase.Discussion) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Discussion) {
                        popUpTo(Screen.ClueRound) { inclusive = true }
                    }
                }
            }
        }

        composable<Screen.Discussion> {
            DiscussionScreen(
                players = gameState.players,
                onStartVoting = {
                    gameViewModel.startVoting()
                    navController.navigate(Screen.Voting)
                }
            )
        }

        composable<Screen.Voting> {
            val currentPhase = gameState.currentPhase
            if (currentPhase is GamePhase.Voting) {
                VotingScreen(
                    players = gameState.players,
                    currentVotes = currentPhase.votes,
                    allowSelfVoting = gameState.settings.allowSelfVoting,
                    currentVoterId = null,
                    onCastVote = { voterId, votedForId ->
                        gameViewModel.castVote(voterId, votedForId)
                    },
                    onFinalizeVoting = {
                        gameViewModel.finalizeVoting()
                    }
                )
            } else if (currentPhase is GamePhase.EliminationReveal) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.EliminationReveal) {
                        popUpTo(Screen.Voting) { inclusive = true }
                    }
                }
            } else if (currentPhase is GamePhase.Discussion) {
                // Tie vote - back to discussion
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Discussion) {
                        popUpTo(Screen.Voting) { inclusive = true }
                    }
                }
            }
        }

        composable<Screen.EliminationReveal> {
            val currentPhase = gameState.currentPhase
            if (currentPhase is GamePhase.EliminationReveal) {
                val eliminatedPlayer = gameState.players.getOrNull(currentPhase.eliminatedPlayerId)
                if (eliminatedPlayer != null) {
                    EliminationRevealScreen(
                        eliminatedPlayer = eliminatedPlayer,
                        onContinue = {
                            gameViewModel.continueAfterElimination()
                        }
                    )
                }
            } else if (currentPhase is GamePhase.GameEnd) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.GameEnd) {
                        popUpTo(Screen.Home)
                    }
                }
            } else if (currentPhase is GamePhase.ClueRound) {
                // Next round
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.ClueRound) {
                        popUpTo(Screen.EliminationReveal) { inclusive = true }
                    }
                }
            }
        }

        composable<Screen.GameEnd> {
            val currentPhase = gameState.currentPhase
            if (currentPhase is GamePhase.GameEnd) {
                GameEndScreen(
                    winner = currentPhase.winner,
                    players = gameState.players,
                    secretWord = gameState.secretWord,
                    roundHistory = gameState.roundHistory,
                    startingPlayerId = gameState.startingPlayerId,
                    onPlayAgain = {
                        gameViewModel.resetGame()
                        navController.navigate(Screen.Setup) {
                            popUpTo(Screen.Home)
                        }
                    },
                    onMainMenu = {
                        gameViewModel.resetGame()
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Home) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable<Screen.About> {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}