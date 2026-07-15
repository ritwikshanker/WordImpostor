package com.deutschdreamers.wordimpostor

import com.deutschdreamers.wordimpostor.data.model.Difficulty
import com.deutschdreamers.wordimpostor.data.model.WordCategory
import com.deutschdreamers.wordimpostor.data.repository.WordRepository
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Regression guards for the word bank. These protect the invariant the game relies on:
 * every difficulty must return a non-blank word, so [WordRepository.getRandomWord] can never
 * hand an empty secret word to a round.
 */
class WordRepositoryTest {

    private val repository = WordRepository()

    @Test
    fun getRandomWord_returnsNonBlank_forEveryDifficulty() {
        Difficulty.entries.forEach { difficulty ->
            // Sample repeatedly since selection is random.
            repeat(200) {
                val word = repository.getRandomWord(difficulty)
                assertTrue(
                    "Blank word returned for $difficulty",
                    word.isNotBlank()
                )
            }
        }
    }

    @Test
    fun getRandomWord_producesVariety_forEveryDifficulty() {
        Difficulty.entries.forEach { difficulty ->
            val distinct = (1..200).map { repository.getRandomWord(difficulty) }.toSet()
            // A healthy pool should yield many distinct words across 200 draws.
            assertTrue(
                "Word pool for $difficulty looks too small (got ${distinct.size} distinct)",
                distinct.size > 5
            )
        }
    }

    @Test
    fun getRandomWord_returnsNonBlankAndVaried_forEveryCategory() {
        WordCategory.entries.forEach { category ->
            val words = (1..200).map { repository.getRandomWord(Difficulty.MEDIUM, category) }
            assertTrue(
                "Blank word returned for category $category",
                words.all { it.isNotBlank() }
            )
            assertTrue(
                "Word pool for $category looks too small (got ${words.toSet().size} distinct)",
                words.toSet().size > 5
            )
        }
    }

    @Test
    fun impostorHint_reflectsCategory_forThemedPacks() {
        WordCategory.entries.filter { it != WordCategory.MIXED }.forEach { category ->
            val hint = repository.impostorHint(Difficulty.MEDIUM, category)
            assertTrue(
                "Hint for $category should mention its name: '$hint'",
                hint.contains(category.displayName)
            )
        }
    }

    @Test
    fun impostorHint_reflectsDifficulty_forMixedPack() {
        val easy = repository.impostorHint(Difficulty.EASY, WordCategory.MIXED)
        val hard = repository.impostorHint(Difficulty.HARD, WordCategory.MIXED)
        // Mixed hint should describe the difficulty band, not name a category.
        assertTrue("Mixed hint should differ by difficulty", easy != hard)
        assertTrue("Easy hint should read as a word clue", easy.contains("word"))
    }
}
