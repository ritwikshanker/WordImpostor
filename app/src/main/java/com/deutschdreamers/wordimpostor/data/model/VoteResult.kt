package com.deutschdreamers.wordimpostor.data.model

data class VoteResult(
    val playerVotes: Map<Int, Int>, // playerId -> vote count
    val eliminatedPlayerId: Int?,
    val isTie: Boolean
)

