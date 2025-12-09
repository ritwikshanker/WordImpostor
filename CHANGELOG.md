# Changelog

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

