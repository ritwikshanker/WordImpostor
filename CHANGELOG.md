# Changelog

## Version 1.1 (2024-12-09)

### New Features

- **Complete Clue History**: View all clues from previous rounds during discussion phase, not just
  the current round
- **Theme Selector**: Switch between Light, Dark, and System themes directly from Settings
- **Enhanced Word Pool**: Added significantly more words to Easy and Medium difficulty levels for
  better variety

### Improvements

- **Better Keyboard Support**:
    - Added "Next" button on keyboard when entering player names for faster setup
    - Fixed keyboard suggestions now work properly when entering clues
- **Improved Game Flow**:
    - Game now properly continues to next round when no one is eliminated (tie votes)
    - Fixed navigation issues when finalizing votes without casting any
- **UI Polish**:
    - System status bar and navigation bar now match app theme colors
    - Better visual distinction between current and previous rounds in clue history
    - Improved contrast and readability in both light and dark themes

### Bug Fixes

- Fixed game hanging when no votes are cast during voting phase
- Fixed game not progressing when tie vote results in no elimination
- Fixed win condition check (Impostors now win correctly when they equal or outnumber Civilians)
- Fixed keyboard suggestions not being accepted in clue input field
- Fixed system bars appearing white in dark theme
- Fixed UI elements being hidden behind system navigation bars on some devices

### Technical Updates

- Updated version code to 2 (version 1.1)
- Improved edge-to-edge display compatibility
- Better state management across game phases

---

## Version 1.0 (Initial Release)

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

