package com.deutschdreamers.wordimpostor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deutschdreamers.wordimpostor.data.model.GameSettings
import com.deutschdreamers.wordimpostor.data.model.ThemeMode
import com.deutschdreamers.wordimpostor.data.model.TieVoteBehavior

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: GameSettings,
    onBack: () -> Unit,
    onUpdateSettings: (GameSettings) -> Unit
) {
    var timerEnabled by remember { mutableStateOf(settings.timerEnabled) }
    var timerDuration by remember { mutableIntStateOf(settings.timerDuration) }
    var allowSelfVoting by remember { mutableStateOf(settings.allowSelfVoting) }
    var tieVoteBehavior by remember { mutableStateOf(settings.tieVoteBehavior) }
    var themeMode by remember { mutableStateOf(settings.themeMode) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
            // Timer Settings
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Timer Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Enable Timer")
                        Switch(
                            checked = timerEnabled,
                            onCheckedChange = { timerEnabled = it }
                        )
                    }

                    if (timerEnabled) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Timer Duration: $timerDuration seconds",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Slider(
                            value = timerDuration.toFloat(),
                            onValueChange = { timerDuration = it.toInt() },
                            valueRange = 15f..120f,
                            steps = 20
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(15, 30, 60, 90).forEach { duration ->
                                FilterChip(
                                    selected = timerDuration == duration,
                                    onClick = { timerDuration = duration },
                                    label = { Text("${duration}s") },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Settings
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Theme",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ThemeMode.entries.forEach { mode ->
                            FilterChip(
                                selected = themeMode == mode,
                                onClick = { themeMode = mode },
                                label = {
                                    Text(
                                        when (mode) {
                                            ThemeMode.SYSTEM -> "System Default"
                                            ThemeMode.LIGHT -> "Light"
                                            ThemeMode.DARK -> "Dark"
                                        }
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Voting Settings
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Voting Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Allow Self-Voting")
                        Switch(
                            checked = allowSelfVoting,
                            onCheckedChange = { allowSelfVoting = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Tie Vote Behavior",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TieVoteBehavior.entries.forEach { behavior ->
                            FilterChip(
                                selected = tieVoteBehavior == behavior,
                                onClick = { tieVoteBehavior = behavior },
                                label = {
                                    Text(
                                        when (behavior) {
                                            TieVoteBehavior.NO_ELIMINATION -> "No Elimination"
                                            TieVoteBehavior.RANDOM_ELIMINATION -> "Random Elimination"
                                            TieVoteBehavior.REVOTE -> "Revote"
                                        }
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    onUpdateSettings(
                        GameSettings(
                            timerEnabled = timerEnabled,
                            timerDuration = timerDuration,
                            difficulty = settings.difficulty,
                            allowSelfVoting = allowSelfVoting,
                            tieVoteBehavior = tieVoteBehavior,
                            themeMode = themeMode
                        )
                    )
                    onBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Save Settings", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

