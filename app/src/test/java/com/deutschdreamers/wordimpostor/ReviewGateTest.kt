package com.deutschdreamers.wordimpostor

import com.deutschdreamers.wordimpostor.review.ReviewGate
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReviewGateTest {

    private val today = 20000L // arbitrary epoch day

    @Test
    fun `does not prompt below the minimum game count`() {
        assertFalse(
            ReviewGate.shouldRequestReview(
                gamesCompleted = ReviewGate.MIN_GAMES_BEFORE_PROMPT - 1,
                lastRequestEpochDay = 0L,
                todayEpochDay = today
            )
        )
    }

    @Test
    fun `prompts on first eligible game when never asked before`() {
        assertTrue(
            ReviewGate.shouldRequestReview(
                gamesCompleted = ReviewGate.MIN_GAMES_BEFORE_PROMPT,
                lastRequestEpochDay = 0L,
                todayEpochDay = today
            )
        )
    }

    @Test
    fun `does not prompt again within the cooldown window`() {
        val recent = today - (ReviewGate.MIN_DAYS_BETWEEN_PROMPTS - 1)
        assertFalse(
            ReviewGate.shouldRequestReview(
                gamesCompleted = ReviewGate.MIN_GAMES_BEFORE_PROMPT + 5,
                lastRequestEpochDay = recent,
                todayEpochDay = today
            )
        )
    }

    @Test
    fun `prompts again once the cooldown has elapsed`() {
        val old = today - ReviewGate.MIN_DAYS_BETWEEN_PROMPTS
        assertTrue(
            ReviewGate.shouldRequestReview(
                gamesCompleted = ReviewGate.MIN_GAMES_BEFORE_PROMPT + 5,
                lastRequestEpochDay = old,
                todayEpochDay = today
            )
        )
    }
}
