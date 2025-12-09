package com.deutschdreamers.wordimpostor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.deutschdreamers.wordimpostor.data.model.Difficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    difficulty: Difficulty,
    onBack: () -> Unit,
    onStartGame: (List<String>, Int) -> Unit
) {
    var playerCount by remember { mutableIntStateOf(4) }
    var impostorCount by remember { mutableIntStateOf(1) }
    var playerNames by remember { mutableStateOf(List(playerCount) { "" }) }
    var selectedDifficulty by remember { mutableStateOf(difficulty) }
    val focusManager = LocalFocusManager.current

    // Update player names list when player count changes
    LaunchedEffect(playerCount) {
        playerNames = List(playerCount) { index ->
            if (index < playerNames.size) playerNames[index] else ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Setup") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Player Count
            Text(
                text = "Number of Players",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (playerCount > 3) playerCount-- },
                    enabled = playerCount > 3
                ) {
                    Text("-", style = MaterialTheme.typography.headlineMedium)
                }

                Text(
                    text = "$playerCount",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = { if (playerCount < 12) playerCount++ },
                    enabled = playerCount < 12
                ) {
                    Icon(Icons.Default.Add, "Increase")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Impostor Count
            Text(
                text = "Number of Impostors",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (impostorCount > 1) impostorCount-- },
                    enabled = impostorCount > 1
                ) {
                    Text("-", style = MaterialTheme.typography.headlineMedium)
                }

                Text(
                    text = "$impostorCount",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = { if (impostorCount < 3 && impostorCount < playerCount - 1) impostorCount++ },
                    enabled = impostorCount < 3 && impostorCount < playerCount - 1
                ) {
                    Icon(Icons.Default.Add, "Increase")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Difficulty
            Text(
                text = "Difficulty",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Difficulty.entries.forEach { diff ->
                    FilterChip(
                        selected = selectedDifficulty == diff,
                        onClick = { selectedDifficulty = diff },
                        label = { Text(diff.name) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Player Names
            Text(
                text = "Player Names",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            playerNames.forEachIndexed { index, name ->
                OutlinedTextField(
                    value = name,
                    onValueChange = { newName ->
                        playerNames = playerNames.toMutableList().apply {
                            this[index] = newName
                        }
                    },
                    label = { Text("Player ${index + 1}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = if (index == playerNames.lastIndex) ImeAction.Done else ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) },
                        onDone = { focusManager.clearFocus() }
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val names = playerNames.mapIndexed { index, name ->
                        name.ifEmpty { "Player ${index + 1}" }
                    }
                    onStartGame(names, impostorCount)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Start Game", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

