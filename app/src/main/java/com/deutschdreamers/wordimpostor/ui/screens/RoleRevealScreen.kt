package com.deutschdreamers.wordimpostor.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import com.deutschdreamers.wordimpostor.data.model.Player
import com.deutschdreamers.wordimpostor.data.model.Role
import com.deutschdreamers.wordimpostor.feedback.LocalGameFeedback
import com.deutschdreamers.wordimpostor.ui.components.PrimaryButton
import kotlinx.coroutines.delay

@Composable
fun RoleRevealScreen(
    currentPlayer: Player,
    secretWord: String,
    impostorHint: String? = null,
    onContinue: () -> Unit
) {
    var showName by remember { mutableStateOf(false) }
    var showReadyButton by remember { mutableStateOf(false) }
    var showRole by remember { mutableStateOf(false) }
    var showPassMessage by remember { mutableStateOf(false) }
    val feedback = LocalGameFeedback.current

    LaunchedEffect(currentPlayer.id) {
        // Reset states for new player
        showName = false
        showReadyButton = false
        showRole = false
        showPassMessage = false

        // Animation sequence
        delay(300)
        showName = true
        delay(800)
        showReadyButton = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Player Name
                AnimatedVisibility(
                    visible = showName,
                    enter = fadeIn(animationSpec = tween(800)) + scaleIn(
                        animationSpec = tween(800),
                        initialScale = 0.8f
                    ),
                    exit = fadeOut()
                ) {
                    Text(
                        text = currentPlayer.name,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Ready Button or Role Reveal
                if (!showRole) {
                    AnimatedVisibility(
                        visible = showReadyButton,
                        enter = fadeIn(animationSpec = tween(500))
                    ) {
                        PrimaryButton(
                            text = "Tap to Reveal Role",
                            onClick = {
                                feedback.heavy()
                                showRole = true
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                } else {
                    AnimatedVisibility(
                        visible = showRole,
                        enter = fadeIn(animationSpec = tween(800)) + scaleIn(
                            animationSpec = tween(800),
                            initialScale = 0.8f
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Role
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (currentPlayer.role == Role.IMPOSTOR)
                                        MaterialTheme.colorScheme.errorContainer
                                    else
                                        MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if (currentPlayer.role == Role.IMPOSTOR)
                                            "You are the"
                                        else
                                            "You are a",
                                        style = MaterialTheme.typography.titleLarge,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = if (currentPlayer.role == Role.IMPOSTOR)
                                            "IMPOSTOR"
                                        else
                                            "CIVILIAN",
                                        style = MaterialTheme.typography.displaySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentPlayer.role == Role.IMPOSTOR)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    if (currentPlayer.role == Role.CIVILIAN) {
                                        Spacer(modifier = Modifier.height(20.dp))

                                        HorizontalDivider()

                                        Spacer(modifier = Modifier.height(20.dp))

                                        Text(
                                            text = "Your word is:",
                                            style = MaterialTheme.typography.bodyLarge,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = secretWord,
                                            style = MaterialTheme.typography.headlineLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = "You must blend in without knowing the word!",
                                            style = MaterialTheme.typography.bodyLarge,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        if (impostorHint != null) {
                                            Spacer(modifier = Modifier.height(16.dp))

                                            Surface(
                                                color = MaterialTheme.colorScheme.surface,
                                                shape = MaterialTheme.shapes.medium,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(16.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Text(
                                                        text = "🔍 Hint",
                                                        style = MaterialTheme.typography.labelLarge,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = impostorHint,
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.error,
                                                        textAlign = TextAlign.Center,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            PrimaryButton(
                                text = "Continue",
                                onClick = { showPassMessage = true },
                                modifier = Modifier.fillMaxWidth(0.8f)
                            )
                        }
                    }
                }
            }

            // Pass message overlay
            AnimatedVisibility(
                visible = showPassMessage,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Pass the phone",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        PrimaryButton(
                            text = "Next Player",
                            onClick = onContinue,
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                }
            }
        }
    }
}

