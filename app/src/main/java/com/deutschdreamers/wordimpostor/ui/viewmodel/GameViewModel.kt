package com.deutschdreamers.wordimpostor.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deutschdreamers.wordimpostor.data.model.*
import com.deutschdreamers.wordimpostor.data.repository.SettingsRepository
import com.deutschdreamers.wordimpostor.data.repository.WordRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val wordRepository: WordRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var timerJob: Job? = null
    private val currentVotes = mutableMapOf<Int, Int>() // voterId -> votedForPlayerId

    init {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                _gameState.value = _gameState.value.copy(settings = settings)
            }
        }
    }

    fun startGame(playerNames: List<String>, impostorCount: Int) {
        // Create players
        val players = playerNames.mapIndexed { index, name ->
            Player(id = index, name = name, role = Role.CIVILIAN)
        }.toMutableList()

        // Randomly assign impostors
        val impostorIndices = players.indices.shuffled().take(impostorCount)
        impostorIndices.forEach { index ->
            players[index] = players[index].copy(role = Role.IMPOSTOR)
        }

        // Select secret word
        val secretWord = wordRepository.getRandomWord(_gameState.value.settings.difficulty)

        // Determine starting player (must be civilian)
        val civilianIndices = players.indices.filter { players[it].role == Role.CIVILIAN }
        val startingPlayerId = civilianIndices.random()

        _gameState.value = GameState(
            players = players,
            secretWord = secretWord,
            currentPhase = GamePhase.RoleReveal(0),
            settings = _gameState.value.settings,
            startingPlayerId = startingPlayerId,
            roundHistory = emptyList()
        )
    }

    fun revealNextRole() {
        val currentPhase = _gameState.value.currentPhase
        if (currentPhase is GamePhase.RoleReveal) {
            val nextIndex = currentPhase.currentPlayerIndex + 1
            if (nextIndex < _gameState.value.players.size) {
                _gameState.value = _gameState.value.copy(
                    currentPhase = GamePhase.RoleReveal(nextIndex)
                )
            } else {
                // All roles revealed, start clue round
                startClueRound()
            }
        }
    }

    private fun startClueRound() {
        val startingPlayerId = _gameState.value.startingPlayerId ?: 0
        _gameState.value = _gameState.value.copy(
            currentPhase = GamePhase.ClueRound(
                currentPlayerIndex = startingPlayerId,
                remainingTime = if (_gameState.value.settings.timerEnabled)
                    _gameState.value.settings.timerDuration else null
            )
        )

        if (_gameState.value.settings.timerEnabled) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            val currentPhase = _gameState.value.currentPhase
            if (currentPhase is GamePhase.ClueRound) {
                var timeRemaining = currentPhase.remainingTime ?: return@launch

                while (timeRemaining > 0) {
                    delay(1000)
                    timeRemaining--
                    _gameState.value = _gameState.value.copy(
                        currentPhase = GamePhase.ClueRound(
                            currentPlayerIndex = currentPhase.currentPlayerIndex,
                            remainingTime = timeRemaining
                        )
                    )
                }

                // Time's up - submit empty clue
                submitClue("")
            }
        }
    }

    fun submitClue(clue: String) {
        timerJob?.cancel()

        val currentPhase = _gameState.value.currentPhase
        if (currentPhase is GamePhase.ClueRound) {
            val currentPlayerId = currentPhase.currentPlayerIndex
            val updatedPlayers = _gameState.value.players.toMutableList()
            updatedPlayers[currentPlayerId] = updatedPlayers[currentPlayerId].copy(
                clue = clue.ifEmpty { "—" }
            )

            // Find next non-eliminated player
            val nextPlayerId = findNextActivePlayer(currentPlayerId)

            if (nextPlayerId == null || hasAllPlayersGivenClues(updatedPlayers)) {
                // All active players have given clues - move to discussion
                _gameState.value = _gameState.value.copy(
                    players = updatedPlayers,
                    currentPhase = GamePhase.Discussion
                )
            } else {
                _gameState.value = _gameState.value.copy(
                    players = updatedPlayers,
                    currentPhase = GamePhase.ClueRound(
                        currentPlayerIndex = nextPlayerId,
                        remainingTime = if (_gameState.value.settings.timerEnabled)
                            _gameState.value.settings.timerDuration else null
                    )
                )

                if (_gameState.value.settings.timerEnabled) {
                    startTimer()
                }
            }
        }
    }

    private fun findNextActivePlayer(currentPlayerId: Int): Int? {
        val players = _gameState.value.players
        var nextId = (currentPlayerId + 1) % players.size
        var attempts = 0

        while (attempts < players.size) {
            if (!players[nextId].isEliminated) {
                return nextId
            }
            nextId = (nextId + 1) % players.size
            attempts++
        }

        return null
    }

    private fun hasAllPlayersGivenClues(players: List<Player>): Boolean {
        return players.all { it.isEliminated || it.clue.isNotEmpty() }
    }

    fun startVoting() {
        currentVotes.clear()
        _gameState.value = _gameState.value.copy(
            currentPhase = GamePhase.Voting(emptyMap())
        )
    }

    fun castVote(voterId: Int, votedForId: Int) {
        currentVotes[voterId] = votedForId

        // Update vote counts
        val voteCounts = mutableMapOf<Int, Int>()
        _gameState.value.players.forEach { player ->
            if (!player.isEliminated) {
                voteCounts[player.id] = 0
            }
        }

        currentVotes.values.forEach { votedForId ->
            voteCounts[votedForId] = (voteCounts[votedForId] ?: 0) + 1
        }

        _gameState.value = _gameState.value.copy(
            currentPhase = GamePhase.Voting(voteCounts)
        )
    }

    fun finalizeVoting() {
        val currentPhase = _gameState.value.currentPhase
        if (currentPhase is GamePhase.Voting) {
            val voteCounts = currentPhase.votes

            if (voteCounts.isEmpty()) {
                // No votes cast - move to discussion
                _gameState.value = _gameState.value.copy(
                    currentPhase = GamePhase.Discussion
                )
                return
            }

            val maxVotes = voteCounts.values.maxOrNull() ?: 0
            val playersWithMaxVotes = voteCounts.filter { it.value == maxVotes }.keys.toList()

            when {
                playersWithMaxVotes.size == 1 -> {
                    // Clear winner
                    eliminatePlayer(playersWithMaxVotes.first())
                }
                _gameState.value.settings.tieVoteBehavior == TieVoteBehavior.RANDOM_ELIMINATION -> {
                    // Random elimination
                    eliminatePlayer(playersWithMaxVotes.random())
                }
                _gameState.value.settings.tieVoteBehavior == TieVoteBehavior.NO_ELIMINATION -> {
                    // No elimination - back to discussion
                    resetClues()
                    _gameState.value = _gameState.value.copy(
                        currentPhase = GamePhase.Discussion
                    )
                }
                else -> {
                    // Revote - back to voting
                    currentVotes.clear()
                    _gameState.value = _gameState.value.copy(
                        currentPhase = GamePhase.Voting(emptyMap())
                    )
                }
            }
        }
    }

    private fun eliminatePlayer(playerId: Int) {
        val updatedPlayers = _gameState.value.players.toMutableList()
        updatedPlayers[playerId] = updatedPlayers[playerId].copy(isEliminated = true)

        // Save round history
        val clues = _gameState.value.players.associate { it.id to it.clue }
        val roundHistory = _gameState.value.roundHistory + RoundHistory(
            roundNumber = _gameState.value.roundHistory.size + 1,
            clues = clues,
            votes = currentVotes.toMap(),
            eliminatedPlayerId = playerId
        )

        _gameState.value = _gameState.value.copy(
            players = updatedPlayers,
            currentPhase = GamePhase.EliminationReveal(playerId),
            roundHistory = roundHistory
        )
    }

    fun continueAfterElimination() {
        val winner = checkWinCondition()

        if (winner != null) {
            _gameState.value = _gameState.value.copy(
                currentPhase = GamePhase.GameEnd(winner)
            )
        } else {
            // Continue to next round
            resetClues()
            startClueRound()
        }
    }

    private fun resetClues() {
        val updatedPlayers = _gameState.value.players.map { it.copy(clue = "") }
        _gameState.value = _gameState.value.copy(players = updatedPlayers)
    }

    private fun checkWinCondition(): Winner? {
        val activePlayers = _gameState.value.players.filter { !it.isEliminated }
        val activeImpostors = activePlayers.count { it.role == Role.IMPOSTOR }
        val activeCivilians = activePlayers.count { it.role == Role.CIVILIAN }

        return when {
            activeImpostors == 0 -> Winner.CIVILIANS
            activeImpostors >= activeCivilians -> Winner.IMPOSTORS
            else -> null
        }
    }

    fun resetGame() {
        timerJob?.cancel()
        currentVotes.clear()
        _gameState.value = GameState(settings = _gameState.value.settings)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

