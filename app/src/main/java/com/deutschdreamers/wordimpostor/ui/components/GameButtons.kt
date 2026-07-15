package com.deutschdreamers.wordimpostor.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deutschdreamers.wordimpostor.feedback.LocalGameFeedback

/** Standard full-width, 56dp-tall primary action button with built-in click feedback. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    val feedback = LocalGameFeedback.current
    Button(
        onClick = {
            feedback.click()
            onClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled,
        colors = colors
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}

/** Standard full-width, 56dp-tall secondary (outlined) action button with click feedback. */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val feedback = LocalGameFeedback.current
    OutlinedButton(
        onClick = {
            feedback.click()
            onClick()
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium)
    }
}
