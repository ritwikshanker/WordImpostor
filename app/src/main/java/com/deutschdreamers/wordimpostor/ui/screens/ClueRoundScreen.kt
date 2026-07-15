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
import com.deutschdreamers.wordimpostor.feedback.LocalGameFeedback
import com.deutschdreamers.wordimpostor.ui.components.CircularTimer
import com.deutschdreamers.wordimpostor.ui.components.PrimaryButton

@Composable
fun ClueRoundScreen(
    currentPlayer: Player,
    secretWord: String,
    remainingTime: Int?,
    totalTime: Int,
    onSubmitClue: (String) -> Unit
) {
    var clueText by remember(currentPlayer.id) { mutableStateOf("") }
    var showWord by remember(currentPlayer.id) { mutableStateOf(false) }
    val feedback = LocalGameFeedback.current

    LaunchedEffect(currentPlayer.id) {
        showWord = false
        kotlinx.coroutines.delay(300)
        showWord = true
    }

    // Subtle tick as the clock runs out.
    LaunchedEffect(remainingTime) {
        if (remainingTime != null && remainingTime in 1..5) {
            feedback.tick()
        }
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
                CircularTimer(
                    remainingSeconds = remainingTime,
                    totalSeconds = totalTime
                )

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
                    // Extract first word if spaces are present (for keyboard suggestions)
                    val newText = if (it.contains(" ")) {
                        it.substringBefore(" ")
                    } else {
                        it
                    }
                    // Allow only single word up to 20 characters
                    if (newText.length <= 20) {
                        clueText = newText
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

            PrimaryButton(
                text = "Submit Clue",
                onClick = { onSubmitClue(clueText.trim()) },
                enabled = clueText.isNotBlank()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    feedback.click()
                    onSubmitClue("")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Skip (No Clue)")
            }
        }
    }
}

