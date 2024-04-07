package com.bballtending.android.feature.addgame.model

import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.player.model.PlayerData

data class AddGameUiState(
    val playingNow: Boolean? = null,
    val gameType: GameType? = null,
    val quarter: Int = 4,
    val quarterMinusEnable: Boolean = true,
    val quarterPlusEnable: Boolean = false,
    val playTime: Int = 10,
    val playTimeMinusEnable: Boolean = true,
    val playTimePlusEnable: Boolean = true,
    val breakTime: Int = 5,
    val breakTimeMinusEnable: Boolean = true,
    val breakTimePlusEnable: Boolean = true,
    val homeTeamPlayer: List<PlayerData> = listOf(),
    val awayTeamPlayer: List<PlayerData> = listOf()
) {
    val startGameEnable: Boolean get() = playingNow != null && gameType != null && homeTeamPlayer.isNotEmpty() && awayTeamPlayer.isNotEmpty()
}
