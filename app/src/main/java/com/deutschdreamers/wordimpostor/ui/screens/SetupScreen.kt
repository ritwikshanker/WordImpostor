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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.deutschdreamers.wordimpostor.R
import com.deutschdreamers.wordimpostor.data.model.Difficulty
import com.deutschdreamers.wordimpostor.data.model.WordCategory
import com.deutschdreamers.wordimpostor.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SetupScreen(
    difficulty: Difficulty,
    category: WordCategory,
    onBack: () -> Unit,
    onStartGame: (List<String>, Int, Difficulty, WordCategory) -> Unit
) {
    var playerCount by remember { mutableIntStateOf(4) }
    var impostorCount by remember { mutableIntStateOf(1) }
    var playerNames by remember { mutableStateOf(List(playerCount) { "" }) }
    var selectedDifficulty by remember { mutableStateOf(difficulty) }
    var selectedCategory by remember { mutableStateOf(category) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val decreasePlayersDesc = stringResource(R.string.setup_players_decrease)
    val decreaseImpostorsDesc = stringResource(R.string.setup_impostors_decrease)

    // Update player names list when player count changes
    LaunchedEffect(playerCount) {
        playerNames = List(playerCount) { index ->
            if (index < playerNames.size) playerNames[index] else ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.setup_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            stringResource(R.string.action_back)
                        )
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
                text = stringResource(R.string.setup_players_label),
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
                    enabled = playerCount > 3,
                    modifier = Modifier.semantics {
                        contentDescription = decreasePlayersDesc
                    }
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
                    Icon(Icons.Default.Add, stringResource(R.string.setup_players_increase))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Impostor Count
            Text(
                text = stringResource(R.string.setup_impostors_label),
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
                    enabled = impostorCount > 1,
                    modifier = Modifier.semantics {
                        contentDescription = decreaseImpostorsDesc
                    }
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
                    Icon(Icons.Default.Add, stringResource(R.string.setup_impostors_increase))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Word Category
            Text(
                text = stringResource(R.string.setup_category_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WordCategory.entries.forEach { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = {
                            Text(
                                stringResource(
                                    R.string.category_chip,
                                    cat.emoji,
                                    stringResource(cat.labelRes)
                                )
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Difficulty (only affects the Mixed pack)
            val difficultyEnabled = selectedCategory == WordCategory.MIXED
            Text(
                text = stringResource(R.string.setup_difficulty_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (difficultyEnabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            if (!difficultyEnabled) {
                Text(
                    text = stringResource(R.string.setup_difficulty_mixed_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Difficulty.entries.forEach { diff ->
                    FilterChip(
                        selected = difficultyEnabled && selectedDifficulty == diff,
                        onClick = { selectedDifficulty = diff },
                        enabled = difficultyEnabled,
                        label = {
                            Text(
                                stringResource(
                                    when (diff) {
                                        Difficulty.EASY -> R.string.difficulty_easy
                                        Difficulty.MEDIUM -> R.string.difficulty_medium
                                        Difficulty.HARD -> R.string.difficulty_hard
                                    }
                                )
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Player Names
            Text(
                text = stringResource(R.string.setup_player_names_label),
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
                    label = { Text(stringResource(R.string.setup_player_hint, index + 1)) },
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

            PrimaryButton(
                text = stringResource(R.string.setup_start_game),
                onClick = {
                    val names = playerNames.mapIndexed { index, name ->
                        name.ifEmpty { context.getString(R.string.setup_player_hint, index + 1) }
                    }
                    onStartGame(names, impostorCount, selectedDifficulty, selectedCategory)
                }
            )
        }
    }
}

