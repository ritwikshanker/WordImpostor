# Word Impostor

A local pass-the-phone social deduction party game built with Kotlin and Jetpack Compose.

## Overview

**Civilians** receive the same secret word. **Impostors** receive nothing and must blend in.  
Players give one-word clues, discuss, and vote to eliminate suspects.

**Win Conditions:**
- Civilians win: All impostors eliminated
- Impostors win: Impostor count ≥ remaining players

## Features

- 3-12 players, 1-3 impostors
- Three difficulty levels (Easy/Medium/Hard word pools)
- Optional timer per clue (15-120 seconds)
- Configurable voting rules (tie-breakers, self-voting)
- Pass-the-phone role reveals with animations
- Material 3 Design with dark mode
- MVVM architecture with StateFlow
- Settings persistence with DataStore
- Rotation-safe state management

## Project Structure

```
app/src/main/java/com/deutschdreamers/wordimpostor/
├── MainActivity.kt
├── data/
│   ├── model/              # Game models (Player, GameState, GamePhase, etc.)
│   └── repository/         # WordRepository, SettingsRepository
├── ui/
│   ├── navigation/         # Type-safe navigation
│   ├── screens/            # All game screens (9 screens)
│   ├── theme/              # Material 3 theming
│   └── viewmodel/          # GameViewModel (state machine)
```

## Tech Stack

- Kotlin 2.0.21
- Jetpack Compose (Material 3)
- Navigation Compose
- Lifecycle ViewModel
- DataStore Preferences
- Kotlinx Serialization

## Building

**Requirements:**
- Android Studio Koala or later
- JDK 11+
- Min SDK: 26 (Android 8.0)
- Target SDK: 35

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
Play Again or Main Menu
```

## Game Rules

### Setup
1. Choose 3-12 players
2. Select 1-3 impostors
3. Pick difficulty (affects word complexity)
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

### Easy (40 words)
Common objects: Apple, Dog, Cat, Tree, House, Car, etc.

### Medium (44 words)
Occupations, tools, household items: Doctor, Teacher, Hammer, Microscope, etc.

### Hard (44 words)
Abstract concepts: Democracy, Justice, Paradox, Entropy, Serendipity, etc.

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
- Difficulty level
- Allow self-voting
- Tie vote behavior

Settings persist across app launches.

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

