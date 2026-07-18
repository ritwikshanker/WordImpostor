# Word Impostor

A local pass-the-phone social deduction word game built with Kotlin and Jetpack Compose.

## Overview

**Civilians** receive the same secret word. **Impostors** receive nothing and must blend in.  
Players give one-word clues, discuss, and vote to eliminate suspects.

**Win Conditions:**
- Civilians win: All impostors eliminated
- Impostors win: Impostor count ≥ remaining players

## Features

**Gameplay**
- 3-12 players, 1-3 impostors
- Three difficulty levels (Easy/Medium/Hard) **and** themed word categories (Animals, Food & Drink,
  Places, Sports, Science & Nature)
- Optional **Impostor Hint** mode — give impostors a subtle clue (category or difficulty band)
- Optional timer per clue (15-120 seconds) with an animated circular countdown
- Configurable voting rules (tie-breakers, self-voting)
- Pass-the-phone role reveals with animations

**Replay & retention**

- Local **stats** (games played, civilian vs. impostor wins, win rate) stored on-device
- **Last-game recap** (winner, secret word, category, roles)
- **Quick Rematch** — replay with the same players and options in one tap

**Look & feel**

- Custom brand palette in light and dark, with optional Material You (dynamic color) on Android 12+
- Sound & haptics feedback (toggleable; respects device settings)
- Smooth slide/fade screen transitions

**Reach & polish**

- Full **English + German** localization, with per-app language on Android 13+
- Tablet / large-screen friendly layout
- Accessibility labels for steppers and the timer
- Google Play in-app review prompt

**Under the hood**
- MVVM architecture with StateFlow
- Settings persistence with DataStore
- State survives rotation and process death (SavedStateHandle)
- Completely offline, ad-free, and open source

## Project Structure

```
app/src/main/java/com/deutschdreamers/wordimpostor/
├── MainActivity.kt
├── data/
│   ├── model/              # Game models (Player, GameState, GamePhase, WordCategory, GameStats, etc.)
│   └── repository/         # WordRepository, SettingsRepository, StatsRepository
├── feedback/              # GameFeedback (sound & haptics)
├── review/                # In-app review gate + controller
├── ui/
│   ├── components/         # Shared UI kit (buttons, circular timer)
│   ├── navigation/         # Type-safe navigation
│   ├── screens/            # All game screens (11 screens)
│   ├── theme/              # Brand palette + Material 3 theming
│   └── viewmodel/          # GameViewModel (state machine)
```

Strings live in `app/src/main/res/values/strings.xml` (English) and
`app/src/main/res/values-de/strings.xml` (German).

## Tech Stack

- Kotlin 2.2.10
- Jetpack Compose (Material 3)
- Navigation Compose (type-safe routes)
- Lifecycle ViewModel + SavedStateHandle
- DataStore Preferences
- Kotlinx Serialization
- Google Play In-App Review

## Building

**Requirements:**

- Android Studio Ladybug or later
- JDK 11+
- Min SDK: 26 (Android 8.0)
- Target SDK: 36

**Steps:**
1. Open project in Android Studio
2. Sync Gradle files
3. Run on device/emulator

Or via terminal:
```bash
./gradlew build
./gradlew installDebug
```

## Game Flow

```
Home Screen
    ↓
Setup Screen (configure players, impostors, difficulty)
    ↓
Role Reveal (pass-the-phone for each player)
    ↓
┌──→ Clue Round (each player gives one-word clue)
│       ↓
│   Discussion (review clues)
│       ↓
│   Voting (eliminate a player)
│       ↓
│   Elimination Reveal
│       ↓
│   Check Win Condition
│       ↓
└───┤ If game continues, next round
    │
    ↓ If game ends
Game End (show winner, roles, history)
    ↓
Rematch (same players), New Game, or Main Menu
```

## Game Rules

### Setup
1. Choose 3-12 players
2. Select 1-3 impostors
3. Pick a word category, or use the Mixed pack with a difficulty (Easy/Medium/Hard)
4. Enter player names

### Role Assignment
- Impostors are randomly assigned
- Starting player is always a Civilian (ensures fair play)
- Roles are revealed one-by-one with pass-the-phone

### Clue Round
- Each player gives one word related to the secret word (if Civilian)
- Impostors must guess appropriate clues to blend in
- Optional timer can be enabled
- Players cannot skip (unless timer expires)

### Discussion
- All players see all clues
- Discuss who might be the impostor
- Optional discussion timer

### Voting
- Each player votes to eliminate one player
- Configurable tie-breaker:
  - **No Elimination**: Tied players safe, new round
  - **Random Elimination**: Random selection from tied players
  - **Revote**: Vote again among tied players only

### Win Conditions
- **Civilians Win**: All impostors eliminated
- **Impostors Win**: Impostor count ≥ remaining player count

## Word Pools

The **Mixed** pack is difficulty-based:

### Easy
Common objects: Apple, Dog, Cat, Tree, House, Car, etc.

### Medium
Occupations, tools, household items: Doctor, Teacher, Hammer, Microscope, etc.

### Hard
Abstract concepts: Democracy, Justice, Paradox, Entropy, Serendipity, etc.

### Themed categories

Each themed pack draws from its own curated list (difficulty doesn't apply):

- 🐾 **Animals** — Dog, Lion, Penguin, Octopus, …
- 🍕 **Food & Drink** — Pizza, Mango, Sushi, Waffle, …
- 🏛️ **Places** — Hospital, Castle, Volcano, Lighthouse, …
- ⚽ **Sports** — Soccer, Tennis, Archery, Snowboarding, …
- 🔬 **Science & Nature** — Gravity, Molecule, Glacier, Photosynthesis, …

## State Management

The app uses a single `GameViewModel` that manages:
- Game phase transitions (sealed class `GamePhase`)
- Player state (clues, elimination status, roles)
- Timer management (coroutines)
- Vote tallying
- Win condition checking
- Round history

State is exposed via `StateFlow` and survives:
- Screen rotations
- Navigation changes
- Temporary app backgrounding

## Settings Persistence

User settings are saved using Jetpack DataStore:
- Timer enabled/disabled
- Timer duration (15-120 seconds)
- Difficulty level and word category
- Impostor Hint mode
- Theme mode and Material You (dynamic color) toggle
- Sound & haptics
- Allow self-voting
- Tie vote behavior

Settings persist across app launches. Local play stats and the last-game recap are stored in a
separate DataStore.

## Localization

Fully translated into **English** and **German**. All user-facing text is externalized to string
resources (`values/` and `values-de/`), so the app follows the device language automatically. On
Android 13+, the app's language can also be set independently via System Settings (backed by
`res/xml/locales_config.xml`). Additional languages only need a new `values-<lang>/strings.xml`.

## Animations

The app includes smooth animations throughout:
- **Fade In/Out**: Role reveals, pass-the-phone transitions
- **Scale In**: Dramatic reveals (role, elimination, winner)
- **Slide In**: Game end details
- **Crossfade**: Phase transitions

All animations use Compose's `AnimatedVisibility` and `AnimatedContent`.

## Download

Available on Google Play Store (coming soon)

## License

This project is **open source** and **completely free** - no ads, no in-app purchases, ever.

Licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest new features
- Submit pull requests
- Improve translations

## Credits

Made with ❤️ by Deutsch Dreamers

Built with:
- Jetpack Compose
- Material 3 Design
- Kotlin Coroutines
- Navigation Compose

---

**Enjoy playing Word Impostor!** 🎮🕵️‍♂️

