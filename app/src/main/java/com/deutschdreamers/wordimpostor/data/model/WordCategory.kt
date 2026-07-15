package com.deutschdreamers.wordimpostor.data.model

/**
 * Selectable word pack for a game. [MIXED] is the classic experience and draws from the
 * difficulty-based word bank; the themed packs draw from their own curated lists (and
 * ignore difficulty). Used as the impostor's hint when hint mode is on.
 */
enum class WordCategory(val displayName: String, val emoji: String) {
    MIXED("Mixed", "🎲"),
    ANIMALS("Animals", "🐾"),
    FOOD("Food & Drink", "🍕"),
    PLACES("Places", "🏛️"),
    SPORTS("Sports", "⚽"),
    SCIENCE("Science & Nature", "🔬")
}
