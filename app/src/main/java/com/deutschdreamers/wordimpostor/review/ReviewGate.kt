package com.deutschdreamers.wordimpostor.review

/**
 * Pure decision logic for when to trigger the Google Play in-app review flow.
 *
 * Kept free of Android dependencies so it can be unit-tested headlessly. The
 * actual review request is delegated to [ReviewController], and Google Play
 * itself applies its own quota on top of this (the dialog may not appear even
 * when [shouldRequestReview] returns true).
 */
object ReviewGate {
    /** Minimum number of completed games before we ever ask for a review. */
    const val MIN_GAMES_BEFORE_PROMPT = 2

    /** Minimum days between two review requests, so we never nag. */
    const val MIN_DAYS_BETWEEN_PROMPTS = 5L

    /**
     * @param gamesCompleted     total games the player has finished
     * @param lastRequestEpochDay epoch day of the previous request (0 = never asked)
     * @param todayEpochDay       today's epoch day
     */
    fun shouldRequestReview(
        gamesCompleted: Int,
        lastRequestEpochDay: Long,
        todayEpochDay: Long
    ): Boolean {
        if (gamesCompleted < MIN_GAMES_BEFORE_PROMPT) return false
        if (lastRequestEpochDay <= 0L) return true
        return todayEpochDay - lastRequestEpochDay >= MIN_DAYS_BETWEEN_PROMPTS
    }
}
