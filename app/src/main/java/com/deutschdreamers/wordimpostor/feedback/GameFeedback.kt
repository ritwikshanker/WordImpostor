package com.deutschdreamers.wordimpostor.feedback

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.SoundEffectConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView

/**
 * Central place for the game's tactile + audible feedback. Uses the platform's
 * own haptic constants and the system click sound effect, so everything honours
 * the user's device-level "touch sounds"/"vibrate on touch" settings and needs
 * no bundled audio assets.
 *
 * Gated by the in-app "Sound & Haptics" setting via [enabled]. Provided through
 * [LocalGameFeedback] so any composable can trigger feedback without threading
 * the flag through its parameters. The [NoOp] instance keeps @Preview and tests
 * from needing a real [View].
 */
class GameFeedback(
    private val view: View?,
    private val enabled: Boolean
) {
    /** Light tap for ordinary button presses; also plays the system click sound. */
    fun click() = perform(HapticFeedbackConstants.KEYBOARD_TAP, playClick = true)

    /** Stronger buzz for meaningful confirmations (e.g. revealing a role). */
    fun heavy() = perform(HapticFeedbackConstants.LONG_PRESS, playClick = false)

    /** Positive outcome (civilians win, correct catch). */
    fun success() = perform(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
        else HapticFeedbackConstants.LONG_PRESS,
        playClick = false
    )

    /** Negative / dramatic outcome (impostors win, elimination). */
    fun reject() = perform(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.REJECT
        else HapticFeedbackConstants.LONG_PRESS,
        playClick = false
    )

    /** Subtle tick for the timer's final seconds. */
    fun tick() = perform(HapticFeedbackConstants.CLOCK_TICK, playClick = false)

    private fun perform(constant: Int, playClick: Boolean) {
        if (!enabled) return
        val v = view ?: return
        v.performHapticFeedback(constant)
        if (playClick) v.playSoundEffect(SoundEffectConstants.CLICK)
    }

    companion object {
        val NoOp = GameFeedback(view = null, enabled = false)
    }
}

val LocalGameFeedback = staticCompositionLocalOf { GameFeedback.NoOp }

@Composable
fun rememberGameFeedback(enabled: Boolean): GameFeedback {
    val view = LocalView.current
    return remember(view, enabled) { GameFeedback(view, enabled) }
}
