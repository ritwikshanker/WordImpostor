package com.deutschdreamers.wordimpostor

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.deutschdreamers.wordimpostor.data.model.*
import com.deutschdreamers.wordimpostor.data.repository.SettingsRepository
import com.deutschdreamers.wordimpostor.data.repository.StatsRepository
import com.deutschdreamers.wordimpostor.data.repository.WordRepository
import com.deutschdreamers.wordimpostor.feedback.LocalGameFeedback
import com.deutschdreamers.wordimpostor.feedback.rememberGameFeedback
import com.deutschdreamers.wordimpostor.review.ReviewController
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
            WordImpostorApp()
        }
    }
}

@Composable
fun WordImpostorApp() {
    val navController = rememberNavController()
    val wordRepository = remember { WordRepository() }
    val settingsRepository = remember { SettingsRepository(navController.context) }
    val statsRepository = remember { StatsRepository(navController.context) }

    // Collect settings to get theme preference
    val settings by settingsRepository.settingsFlow.collectAsState(initial = GameSettings())

    // Determine dark theme based on user preference
    val systemInDarkTheme = isSystemInDarkTheme()
    val darkTheme = when (settings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> systemInDarkTheme
    }

    WordImpostorTheme(darkTheme = darkTheme, dynamicColor = settings.dynamicColor) {
        // With enableEdgeToEdge() the system bars are transparent and draw over app
        // content (Android 15+ contract); insets are consumed via safeDrawingPadding().
        // We only need to set the bar icon contrast: dark icons on a light theme,
        // light icons on a dark theme.
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = !darkTheme
                insetsController.isAppearanceLightNavigationBars = !darkTheme
            }
        }

        // Full-bleed themed background so the transparent status/navigation bar
        // regions show the app's background color (following the in-app dark/light
        // setting) instead of the white window background. The content inside is
        // inset away from the bars via safeDrawingPadding().
        val feedback = rememberGameFeedback(settings.soundHapticsEnabled)
        CompositionLocalProvider(LocalGameFeedback provides feedback) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                // Constrain content to a comfortable reading width and centre it, so
                // the phone-first layouts don't stretch across large tablet screens.
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .widthIn(max = 640.dp)
                    ) {
                        WordImpostorAppContent(
                            settingsRepository,
                            wordRepository,
                            statsRepository,
                            navController
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WordImpostorAppContent(
    settingsRepository: SettingsRepository,
    wordRepository: WordRepository,
    statsRepository: StatsRepository,
    navController: androidx.navigation.NavHostController
) {

    val gameViewModel: GameViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                GameViewModel(wordRepository, settingsRepository, createSavedStateHandle())
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
            .safeDrawingPadding(),
        enterTransition = {
            slideInHorizontally(animationSpec = tween(300)) { it / 4 } + fadeIn(tween(300))
        },
        exitTransition = {
            slideOutHorizontally(animationSpec = tween(300)) { -it / 6 } + fadeOut(tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(animationSpec = tween(300)) { -it / 6 } + fadeIn(tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(animationSpec = tween(300)) { it / 4 } + fadeOut(tween(300))
        }
    ) {
        composable<Screen.Home> {
            HomeScreen(
                onNewGame = { navController.navigate(Screen.Setup) },
                onSettings = { navController.navigate(Screen.Settings) },
                onAbout = { navController.navigate(Screen.About) },
                onStats = { navController.navigate(Screen.Stats) }
            )
        }

        composable<Screen.Setup> {
            SetupScreen(
                difficulty = gameState.settings.difficulty,
                category = gameState.settings.wordCategory,
                onBack = { navController.popBackStack() },
                onStartGame = { playerNames, impostorCount, difficulty, category ->
                    gameViewModel.startGame(playerNames, impostorCount, difficulty, category)
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
                        impostorHint = impostorHintText(gameState.settings),
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
                        totalTime = gameState.settings.timerDuration,
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
                roundHistory = gameState.roundHistory,
                currentRoundNumber = gameState.roundHistory.size + 1,
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
            } else if (currentPhase is GamePhase.ClueRound) {
                // No votes or no elimination - start new round
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.ClueRound) {
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
                // A finished game is a positive moment: record stats + recap, count
                // it for the review gate, and (when eligible) ask for a Play Store
                // review. All best-effort, so it never interrupts gameplay.
                val context = LocalContext.current
                val category = gameState.settings.wordCategory
                val categoryLabel = stringResource(
                    R.string.category_chip,
                    category.emoji,
                    stringResource(category.labelRes)
                )
                LaunchedEffect(Unit) {
                    val recap = GameRecap(
                        winner = currentPhase.winner,
                        secretWord = gameState.secretWord,
                        categoryLabel = categoryLabel,
                        players = gameState.players.map {
                            PlayerSummary(it.name, it.role, it.isEliminated)
                        },
                        rounds = gameState.roundHistory.size
                    )
                    statsRepository.recordGame(recap)
                    settingsRepository.incrementGamesCompleted()
                    (context as? Activity)?.let { activity ->
                        ReviewController.maybeRequestReview(activity, settingsRepository)
                    }
                }
                GameEndScreen(
                    winner = currentPhase.winner,
                    players = gameState.players,
                    secretWord = gameState.secretWord,
                    roundHistory = gameState.roundHistory,
                    startingPlayerId = gameState.startingPlayerId,
                    onRematch = {
                        gameViewModel.rematch()
                        navController.navigate(Screen.RoleReveal) {
                            popUpTo(Screen.Home)
                        }
                    },
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

        composable<Screen.Stats> {
            val stats by statsRepository.statsFlow.collectAsState(initial = GameStats())
            val lastRecap by statsRepository.lastRecapFlow.collectAsState(initial = null)
            StatsScreen(
                stats = stats,
                lastRecap = lastRecap,
                onBack = { navController.popBackStack() },
                onResetStats = {
                    scope.launch { statsRepository.resetStats() }
                }
            )
        }
    }
}

/**
 * Builds the localized impostor hint shown on the role-reveal screen (category for a
 * themed pack, difficulty band for a mixed game), or null when hint mode is off.
 */
@Composable
private fun impostorHintText(settings: GameSettings): String? {
    if (!settings.impostorHintEnabled) return null
    return if (settings.wordCategory == WordCategory.MIXED) {
        stringResource(
            when (settings.difficulty) {
                Difficulty.EASY -> R.string.impostor_hint_easy
                Difficulty.MEDIUM -> R.string.impostor_hint_medium
                Difficulty.HARD -> R.string.impostor_hint_hard
            }
        )
    } else {
        val label = stringResource(
            R.string.category_chip,
            settings.wordCategory.emoji,
            stringResource(settings.wordCategory.labelRes)
        )
        stringResource(R.string.impostor_hint_category, label)
    }
}