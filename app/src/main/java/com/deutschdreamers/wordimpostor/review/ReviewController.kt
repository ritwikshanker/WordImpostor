package com.deutschdreamers.wordimpostor.review

import android.app.Activity
import android.util.Log
import com.deutschdreamers.wordimpostor.data.repository.SettingsRepository
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory
import java.time.LocalDate

/**
 * Launches the Google Play in-app review flow at natural, non-intrusive moments
 * (currently: after a game finishes), gated by [ReviewGate].
 *
 * The Play API is best-effort: it silently no-ops on devices without Play (or
 * when Google's own quota is exhausted), so this never blocks or interrupts the
 * game. We record the request timestamp regardless, so we respect our own
 * cooldown even when Play decides not to surface the card.
 */
object ReviewController {

    private const val TAG = "ReviewController"

    suspend fun maybeRequestReview(
        activity: Activity,
        settingsRepository: SettingsRepository,
        today: LocalDate = LocalDate.now()
    ) {
        val gamesCompleted = settingsRepository.getGamesCompleted()
        val lastRequestEpochDay = settingsRepository.getLastReviewEpochDay()
        val todayEpochDay = today.toEpochDay()

        if (!ReviewGate.shouldRequestReview(gamesCompleted, lastRequestEpochDay, todayEpochDay)) {
            return
        }

        try {
            val manager = ReviewManagerFactory.create(activity)
            val reviewInfo = manager.requestReview()
            manager.launchReview(activity, reviewInfo)
            // Record only after a successful launch so a transient failure can retry.
            settingsRepository.recordReviewRequested(todayEpochDay)
        } catch (e: Exception) {
            // Never let a review failure affect gameplay.
            Log.w(TAG, "In-app review flow failed or unavailable", e)
        }
    }
}
