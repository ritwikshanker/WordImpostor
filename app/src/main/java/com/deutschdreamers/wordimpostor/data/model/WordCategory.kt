package com.deutschdreamers.wordimpostor.data.model

import androidx.annotation.StringRes
import com.deutschdreamers.wordimpostor.R
import kotlinx.serialization.Serializable

/**
 * Selectable word pack for a game. [MIXED] is the classic experience and draws from the
 * difficulty-based word bank; the themed packs draw from their own curated lists (and
 * ignore difficulty). Used as the impostor's hint when hint mode is on.
 *
 * [labelRes] is the localized display name; [emoji] prefixes it in the UI.
 */
@Serializable
enum class WordCategory(val emoji: String, @StringRes val labelRes: Int) {
    MIXED("🎲", R.string.category_mixed),
    ANIMALS("🐾", R.string.category_animals),
    FOOD("🍕", R.string.category_food),
    PLACES("🏛️", R.string.category_places),
    SPORTS("⚽", R.string.category_sports),
    SCIENCE("🔬", R.string.category_science)
}
