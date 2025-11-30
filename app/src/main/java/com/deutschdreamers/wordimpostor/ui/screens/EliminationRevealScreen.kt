package com.deutschdreamers.wordimpostor.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deutschdreamers.wordimpostor.data.model.Player
import com.deutschdreamers.wordimpostor.data.model.Role
import kotlinx.coroutines.delay

@Composable
fun EliminationRevealScreen(
    eliminatedPlayer: Player,
    onContinue: () -> Unit
) {
    var showName by remember { mutableStateOf(false) }
    var showRole by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showName = true
        delay(1000)
        showRole = true
        delay(1000)
        showButton = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = showName,
                enter = fadeIn(animationSpec = tween(800)) + scaleIn(
                    animationSpec = tween(800)
                )
            ) {
                Text(
                    text = "${eliminatedPlayer.name}\nhas been eliminated",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = showRole,
                enter = fadeIn(animationSpec = tween(800)) + scaleIn(
                    animationSpec = tween(800)
                )
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (eliminatedPlayer.role == Role.IMPOSTOR)
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
                            text = "They were a",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = if (eliminatedPlayer.role == Role.IMPOSTOR)
                                "IMPOSTOR"
                            else
                                "CIVILIAN",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (eliminatedPlayer.role == Role.IMPOSTOR)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(animationSpec = tween(500))
            ) {
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text("Continue", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

