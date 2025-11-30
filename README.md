# Word Impostor - Android Party Game

A complete pass-the-phone social deduction party game built with Kotlin and Jetpack Compose.

## Game Description

Word Impostor is a local multiplayer social deduction game where:
- **Civilians** receive the same secret word
- **Impostors** receive nothing and must blend in
- Players take turns giving one-word clues
- Players discuss and vote to eliminate suspects
- **Civilians win** if all impostors are eliminated
- **Impostors win** if their count equals or exceeds remaining players

## Features

### Core Gameplay
- ✅ 3-12 players support
- ✅ 1-3 impostors
- ✅ Pass-the-phone role reveal with animations
- ✅ One-word clue system
- ✅ Discussion phase
- ✅ Voting with configurable tie-breaker rules
- ✅ Automatic win condition detection

### Game Settings
- ✅ Timer per clue (15-120 seconds, or disabled)
- ✅ Three difficulty levels (Easy/Medium/Hard word pools)
- ✅ Allow/disallow self-voting
- ✅ Tie vote behavior (No elimination / Random / Revote)
- ✅ Settings persistence with DataStore

### UI/UX
- ✅ Material 3 Design
- ✅ Dark mode support
- ✅ Smooth animations (fade, scale, slide)
- ✅ Pass-the-phone enforced screens
- ✅ End game summary with full history

### Technical
- ✅ MVVM Architecture
- ✅ Single Activity with Navigation Compose
- ✅ StateFlow for reactive state management
- ✅ Rotation-safe (ViewModel state preservation)
- ✅ Type-safe navigation with Kotlin Serialization

## Project Structure

```
app/src/main/java/com/deutschdreamers/wordimpostor/
├── MainActivity.kt                      # Single Activity entry point
├── data/
│   ├── model/
│   │   ├── Difficulty.kt               # Word difficulty levels
│   │   ├── GamePhase.kt                # Game state machine phases
│   │   ├── GameSettings.kt             # User configurable settings
│   │   ├── GameState.kt                # Complete game state
│   │   ├── Player.kt                   # Player data model
│   │   ├── Role.kt                     # Civilian/Impostor roles
│   │   └── VoteResult.kt               # Voting result data
│   └── repository/
│       ├── SettingsRepository.kt       # DataStore settings persistence
│       └── WordRepository.kt           # Word pools (40+ words per difficulty)
├── ui/
│   ├── navigation/
│   │   └── Screen.kt                   # Type-safe navigation routes
│   ├── screens/
│   │   ├── HomeScreen.kt               # Main menu
│   │   ├── SetupScreen.kt              # Player/game configuration
│   │   ├── SettingsScreen.kt           # Game settings
│   │   ├── RoleRevealScreen.kt         # Animated role reveals
│   │   ├── ClueRoundScreen.kt          # Clue giving phase
│   │   ├── DiscussionScreen.kt         # Clue review & discussion
│   │   ├── VotingScreen.kt             # Voting phase
│   │   ├── EliminationRevealScreen.kt  # Elimination animation
│   │   └── GameEndScreen.kt            # Winner & game summary
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt
│   │   └── Type.kt
│   └── viewmodel/
│       └── GameViewModel.kt            # Complete game logic & state machine
```

## Dependencies

All dependencies are managed in `gradle/libs.versions.toml`:

- **Kotlin**: 2.0.21
- **Compose BOM**: 2024.09.00
- **Navigation Compose**: 2.8.5
- **Lifecycle ViewModel Compose**: 2.9.4
- **DataStore Preferences**: 1.1.1
- **Kotlinx Serialization**: 1.7.3

## Building the Project

### Requirements
- Android Studio Koala (2024.1.1) or later
- JDK 11 or higher
- Android SDK 35
- Minimum Android API 26 (Android 8.0)

### Steps

1. **Open in Android Studio**
   ```
   File > Open > Select WordImpostor folder
   ```

2. **Sync Gradle**
   - Android Studio will automatically prompt to sync
   - Or click: File > Sync Project with Gradle Files

3. **Build the project**
   ```
   Build > Make Project
   ```
   Or use terminal:
   ```bash
   ./gradlew build
   ```

4. **Run on device/emulator**
   ```
   Run > Run 'app'
   ```
   Or use terminal:
   ```bash
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

## Testing

The game can be tested with:
- **Minimum players**: 3 (2 civilians, 1 impostor)
- **Maximum players**: 12
- **Typical game**: 4-6 players, 1-2 impostors
- **Quick test**: 3 players, 1 impostor, Easy difficulty, no timer

## Known Limitations

- Local play only (pass-the-phone)
- No AI players
- English language only
- Word pools are hardcoded (not user-expandable)

## Future Enhancements

Potential features for future versions:
- Custom word lists
- Multiple language support
- Game statistics tracking
- Player profiles
- Network multiplayer
- Word category selection
- Accessibility improvements (TalkBack, large text)

## Architecture Decisions

### Why Single Activity?
- Simpler navigation state management
- Better shared ViewModel scope
- Modern Android best practice

### Why StateFlow over LiveData?
- Better Kotlin coroutines integration
- Null safety
- More flexible operators

### Why Hardcoded Word Lists?
- No external dependencies
- Offline-first
- Predictable game experience
- Easy to modify/extend

### Why DataStore over SharedPreferences?
- Coroutine support
- Type safety
- Observability

## License

This project is created for educational purposes.

## Credits

Built with:
- Jetpack Compose
- Material 3 Design
- Kotlin Coroutines
- Navigation Compose

---

**Enjoy playing Word Impostor!** 🎮🕵️‍♂️

