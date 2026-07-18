package com.deutschdreamers.wordimpostor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.deutschdreamers.wordimpostor.R
import com.deutschdreamers.wordimpostor.ui.components.PrimaryButton
import com.deutschdreamers.wordimpostor.ui.components.SecondaryButton

@Composable
fun HomeScreen(
    onNewGame: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit = {},
    onStats: () -> Unit = {}
) {
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
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.home_tagline),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            PrimaryButton(text = stringResource(R.string.home_new_game), onClick = onNewGame)

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(text = stringResource(R.string.home_stats), onClick = onStats)

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(text = stringResource(R.string.home_settings), onClick = onSettings)

            Spacer(modifier = Modifier.height(16.dp))

            SecondaryButton(text = stringResource(R.string.home_about), onClick = onAbout)

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.home_how_to_play_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.home_how_to_play_body),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


