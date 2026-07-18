package com.deutschdreamers.wordimpostor.ui.screens

import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deutschdreamers.wordimpostor.R
import com.deutschdreamers.wordimpostor.data.model.GameSettings
import com.deutschdreamers.wordimpostor.data.model.ThemeMode
import com.deutschdreamers.wordimpostor.data.model.TieVoteBehavior
import com.deutschdreamers.wordimpostor.ui.components.PrimaryButton

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
    var useDynamicColor by remember { mutableStateOf(settings.dynamicColor) }
    var soundHapticsEnabled by remember { mutableStateOf(settings.soundHapticsEnabled) }
    var impostorHintEnabled by remember { mutableStateOf(settings.impostorHintEnabled) }
    val dynamicColorSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
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
            // Timer Settings
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings_timer_section),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.settings_enable_timer))
                        Switch(
                            checked = timerEnabled,
                            onCheckedChange = { timerEnabled = it }
                        )
                    }

                    if (timerEnabled) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.settings_timer_duration, timerDuration),
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
                                    label = {
                                        Text(
                                            stringResource(
                                                R.string.settings_duration_chip,
                                                duration
                                            )
                                        )
                                    },
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
                        text = stringResource(R.string.settings_theme_section),
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
                                        stringResource(
                                            when (mode) {
                                                ThemeMode.SYSTEM -> R.string.theme_system
                                                ThemeMode.LIGHT -> R.string.theme_light
                                                ThemeMode.DARK -> R.string.theme_dark
                                            }
                                        )
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    if (dynamicColorSupported) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(stringResource(R.string.settings_use_device_colors))
                                Text(
                                    text = stringResource(R.string.settings_use_device_colors_desc),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = useDynamicColor,
                                onCheckedChange = { useDynamicColor = it }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sound & Haptics
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(R.string.settings_sound_haptics),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.settings_sound_haptics_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = soundHapticsEnabled,
                            onCheckedChange = { soundHapticsEnabled = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gameplay
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings_gameplay_section),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stringResource(R.string.settings_impostor_hint))
                            Text(
                                text = stringResource(R.string.settings_impostor_hint_desc),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = impostorHintEnabled,
                            onCheckedChange = { impostorHintEnabled = it }
                        )
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
                        text = stringResource(R.string.settings_voting_section),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(R.string.settings_allow_self_voting))
                        Switch(
                            checked = allowSelfVoting,
                            onCheckedChange = { allowSelfVoting = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.settings_tie_behavior),
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
                                        stringResource(
                                            when (behavior) {
                                                TieVoteBehavior.NO_ELIMINATION -> R.string.tie_no_elimination
                                                TieVoteBehavior.RANDOM_ELIMINATION -> R.string.tie_random_elimination
                                                TieVoteBehavior.REVOTE -> R.string.tie_revote
                                            }
                                        )
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(R.string.settings_save),
                onClick = {
                    onUpdateSettings(
                        GameSettings(
                            timerEnabled = timerEnabled,
                            timerDuration = timerDuration,
                            difficulty = settings.difficulty,
                            wordCategory = settings.wordCategory,
                            impostorHintEnabled = impostorHintEnabled,
                            allowSelfVoting = allowSelfVoting,
                            tieVoteBehavior = tieVoteBehavior,
                            themeMode = themeMode,
                            dynamicColor = useDynamicColor,
                            soundHapticsEnabled = soundHapticsEnabled
                        )
                    )
                    onBack()
                }
            )
        }
    }
}

