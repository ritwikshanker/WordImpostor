# Changelog

## Version 3.3.0 (2026-07-15)

### ✨ Feel & Polish

- **Smooth screen transitions**: screens now slide and fade as you move through the game, instead of
  snapping between phases.
- **Sound & haptics**: subtle vibration and tap sounds add feedback to button presses, role reveals,
  eliminations, and the win screen. Toggle it all under **Settings → Sound & Haptics** (it also
  respects your phone's system touch-sound and vibration settings).
- **New circular countdown timer**: the clue-round timer is now a clean animated ring that fills
  down
  and shifts colour as the seconds run out, with a gentle tick in the final five seconds.
- **More consistent buttons** across every screen for a tidier, more polished look.

### 🔧 Technical Improvements

- Version updated to 3.3.0 (version code 7).
- Introduced a shared UI component kit (buttons, circular timer) and a central sound/haptics helper.

---

## Version 3.2.0 (2026-07-12)

### 🎨 Brand Redesign

- **New custom color palette**: Word Impostor now has its own identity — a mystery-violet primary,
  fresh teal, and warm amber accents — replacing the default template colors in both light and dark
  modes.
- **Refined typography**: bolder, tighter headings and a fuller type scale for a more polished,
  "party game" feel.
- **Material You is now opt-in**: the app ships with the new brand look by default. On Android 12+,
  turn on **Settings → Use device colors** to match your phone's palette instead.
- **Cold-start polish**: the launch background now matches the brand surface colors, so there's no
  color flash before the app draws.

### ⭐ Rate the Game

- Added a Google Play in-app review prompt that appears at a natural moment — after you've finished
  a
  few games — so you can rate Word Impostor without leaving the app.

### 🐛 Bug Fixes

- Fixed the default theme setting not matching what the app actually applied on first launch.

### 🔧 Technical Improvements

- Version updated to 3.2.0 (version code 6).
- Integrated the Google Play In-App Review library.

---

## Version 3.1 (2025-12-09)

### 🎨 UI/UX Improvements

- **Complete Clue History**: View all clues from previous rounds during discussion phase, not just
  the current round
    - Previous rounds shown with distinct visual styling
    - Current round highlighted in primary color
    - Easy to scroll through full game history
- **Theme Selector**: Added ability to switch between Light, Dark, and System themes in Settings
- **Default Theme**: Changed default theme to Light mode for better out-of-box experience
- **Better Visual Hierarchy**: Improved distinction between current and past rounds in clue history

### ⌨️ Keyboard & Input Improvements

- **Smart Keyboard Navigation**:
    - Added "Next" button on keyboard when entering player names for faster setup
  - Last field shows "Done" button to dismiss keyboard
  - Auto-focus moves to next field when pressing "Next"
- **Keyboard Suggestions Fixed**:
    - Keyboard word suggestions now work properly when entering clues
    - Tapping suggestions automatically extracts first word

### 🎮 Gameplay Fixes

- **Win Condition Fixed**: Impostors now correctly win when they equal or outnumber Civilians (not
  just when they outnumber)
- **No Elimination Flow**: Game properly continues to next round when no one is eliminated due to
  tie votes
- **No Votes Cast**: Fixed game hanging when voting is finalized without any votes being cast
- **Navigation Improvements**: Smooth transitions between all game phases

### 📝 Enhanced Word Pool

- **Easy Difficulty**: Added 50+ new words including more animals, fruits, foods, and everyday
  objects
- **Medium Difficulty**: Added 50+ new words including more occupations, tools, and common items
- **Better Variety**: Reduced repetition and improved word selection across all difficulty levels

### 🐛 Bug Fixes

- Fixed game hanging on white screen when finalizing votes without casting any
- Fixed game not progressing when tie vote behavior is set to NO_ELIMINATION
- Fixed keyboard suggestions not being accepted in clue input field
- Fixed UI elements being hidden behind system navigation bars on some devices
- Fixed edge-to-edge display issues on phones with gesture navigation

### ℹ️ About Screen Updates

- Updated version number to 3.1
- Updated creator information to Ritwik Shanker
- Updated copyright information

### 🔧 Technical Improvements

- Version code updated to 4 (version 3.1)
- Improved state management for round history
- Better handling of edge cases in voting phase
- Enhanced navigation flow between game phases
- Improved theme switching responsiveness

---

## Version 3.0 (Initial Release - First Public Release)

### Features

- Local multiplayer social deduction game for 3-12 players
- Three difficulty levels (Easy, Medium, Hard)
- Configurable game settings:
    - Number of players (3-12)
    - Number of impostors (1-3)
    - Timer per clue
    - Self-voting toggle
    - Tie vote behavior (No elimination, Random, Revote)
- Role reveal with pass-the-phone mechanic
- Clue round with optional timer
- Discussion and voting phases
- Complete game history and statistics at end
- Dark mode support
- Material You design
- Completely ad-free and open source

