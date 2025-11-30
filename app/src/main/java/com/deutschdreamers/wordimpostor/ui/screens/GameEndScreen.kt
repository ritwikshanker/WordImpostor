package com.deutschdreamers.wordimpostor.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deutschdreamers.wordimpostor.data.model.*
import kotlinx.coroutines.delay

@Composable
fun GameEndScreen(
    winner: Winner,
    players: List<Player>,
    secretWord: String,
    roundHistory: List<RoundHistory>,
    startingPlayerId: Int?,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    var showWinner by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showWinner = true
        delay(1000)
        showDetails = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Winner Announcement
            AnimatedVisibility(
                visible = showWinner,
                enter = fadeIn(animationSpec = tween(1000)) + scaleIn(
                    animationSpec = tween(1000)
                )
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (winner == Winner.IMPOSTORS)
                            MaterialTheme.colorScheme.errorContainer
                        else
                            MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Game Over!",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (winner == Winner.IMPOSTORS)
                                "IMPOSTORS WIN!"
                            else
                                "CIVILIANS WIN!",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (winner == Winner.IMPOSTORS)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Game Details
            AnimatedVisibility(
                visible = showDetails,
                enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
                    animationSpec = tween(800)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Secret Word
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "The Secret Word Was:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = secretWord,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Player Roles
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Player Roles:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            players.forEach { player ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = player.name,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = if (player.role == Role.IMPOSTOR) "IMPOSTOR" else "Civilian",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = if (player.role == Role.IMPOSTOR)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Starting Player
                    if (startingPlayerId != null) {
                        val startingPlayer = players.find { it.id == startingPlayerId }
                        if (startingPlayer != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Text(
                                    text = "Starting Player: ${startingPlayer.name}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Round History
                    if (roundHistory.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Game Summary:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                roundHistory.forEach { round ->
                                    Text(
                                        text = "Round ${round.roundNumber}",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))

                                    round.clues.forEach { (playerId, clue) ->
                                        val player = players.find { it.id == playerId }
                                        if (player != null && clue.isNotEmpty()) {
                                            Text(
                                                text = "${player.name}: $clue",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }

                                    if (round.eliminatedPlayerId != null) {
                                        val eliminatedPlayer = players.find { it.id == round.eliminatedPlayerId }
                                        if (eliminatedPlayer != null) {
                                            Text(
                                                text = "Eliminated: ${eliminatedPlayer.name}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }

                                    if (round != roundHistory.last()) {
                                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Play Again", style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onMainMenu,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Main Menu", style = MaterialTheme.typography.titleMedium)
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

