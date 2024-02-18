package com.bballtending.android.domain.game.model

import com.bballtending.android.domain.player.model.PlayerData

data class GameData(
    val gameId: Long,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val gameType: GameType,
    val quarter: Int,
    val playTime: Int,
    val breakTime: Int,
    val homeTeamName: String,
    val awayTeamName: String,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val homeTeamPlayer: List<PlayerData>,
    val awayTeamPlayer: List<PlayerData>
)