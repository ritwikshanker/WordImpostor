package com.deutschdreamers.wordimpostor.ui.screens

import androidx.compose.animation.*
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

@Composable
fun ClueRoundScreen(
    currentPlayer: Player,
    secretWord: String,
    remainingTime: Int?,
    onSubmitClue: (String) -> Unit
) {
    var clueText by remember(currentPlayer.id) { mutableStateOf("") }
    var showWord by remember(currentPlayer.id) { mutableStateOf(false) }

    LaunchedEffect(currentPlayer.id) {
        showWord = false
        kotlinx.coroutines.delay(300)
        showWord = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Timer
            if (remainingTime != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            remainingTime <= 5 -> MaterialTheme.colorScheme.errorContainer
                            remainingTime <= 10 -> MaterialTheme.colorScheme.tertiaryContainer
                            else -> MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                ) {
                    Text(
                        text = "$remainingTime",
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(24.dp),
                        color = when {
                            remainingTime <= 5 -> MaterialTheme.colorScheme.error
                            remainingTime <= 10 -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.primary
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Player Name
            Text(
                text = "${currentPlayer.name}'s Turn",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Clue Input
            Text(
                text = "Enter your one-word clue:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = clueText,
                onValueChange = {
                    // Allow only single word (no spaces)
                    if (!it.contains(" ") && it.length <= 20) {
                        clueText = it
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                placeholder = {
                    Text(
                        "Type one word...",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "One word only, no spaces",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { onSubmitClue(clueText.trim()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = clueText.isNotBlank()
            ) {
                Text("Submit Clue", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { onSubmitClue("") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Skip (No Clue)")
            }
        }
    }
}

