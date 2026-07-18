package com.deutschdreamers.wordimpostor

import com.deutschdreamers.wordimpostor.data.model.GameStats
import com.deutschdreamers.wordimpostor.data.model.Winner
import org.junit.Assert.assertEquals
import org.junit.Test

class GameStatsTest {

    @Test
    fun recordedWith_countsCivilianWin() {
        val stats = GameStats().recordedWith(Winner.CIVILIANS)
        assertEquals(1, stats.gamesPlayed)
        assertEquals(1, stats.civilianWins)
        assertEquals(0, stats.impostorWins)
    }

    @Test
    fun recordedWith_countsImpostorWin() {
        val stats = GameStats().recordedWith(Winner.IMPOSTORS)
        assertEquals(1, stats.gamesPlayed)
        assertEquals(0, stats.civilianWins)
        assertEquals(1, stats.impostorWins)
    }

    @Test
    fun recordedWith_accumulatesAcrossGames() {
        val stats = GameStats()
            .recordedWith(Winner.CIVILIANS)
            .recordedWith(Winner.CIVILIANS)
            .recordedWith(Winner.IMPOSTORS)
        assertEquals(3, stats.gamesPlayed)
        assertEquals(2, stats.civilianWins)
        assertEquals(1, stats.impostorWins)
    }

    @Test
    fun civilianWinRate_isZeroWithNoGames() {
        assertEquals(0f, GameStats().civilianWinRate, 0.0001f)
    }

    @Test
    fun civilianWinRate_computesShare() {
        val stats = GameStats(gamesPlayed = 4, civilianWins = 3, impostorWins = 1)
        assertEquals(0.75f, stats.civilianWinRate, 0.0001f)
    }
}
