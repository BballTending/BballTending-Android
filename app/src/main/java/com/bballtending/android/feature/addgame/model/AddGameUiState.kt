package com.bballtending.android.feature.addgame.model

import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.player.model.PlayerData

data class AddGameUiState(
    val playingNow: Boolean = true,
    val hour: Int = 0,
    val minute: Int = 0,
    val gameType: GameType = GameType.FULL_COURT,
    val quarter: Int = 4,
    val quarterMinusEnable: Boolean = true,
    val quarterPlusEnable: Boolean = false,
    val playTime: Int = 10,
    val playTimeMinusEnable: Boolean = true,
    val playTimePlusEnable: Boolean = true,
    val breakTime: Int = 5,
    val breakTimeMinusEnable: Boolean = true,
    val breakTimePlusEnable: Boolean = true,
    val targetScore: Int = 21,
    val targetScoreMinusEnable: Boolean = true,
    val targetScorePlusEnable: Boolean = false,
    val homeTeamPlayer: List<PlayerData> = listOf(),
    val awayTeamPlayer: List<PlayerData> = listOf()
) {
    fun withGameType(gameType: GameType): AddGameUiState = when (gameType) {
        GameType.HALF_COURT -> {
            copy(
                quarter = 1,
                quarterMinusEnable = false,
                quarterPlusEnable = true,
                playTime = 10,
                playTimeMinusEnable = true,
                playTimePlusEnable = true,
                breakTime = 5,
                breakTimeMinusEnable = true,
                breakTimePlusEnable = true,
                targetScore = 21,
                targetScoreMinusEnable = true,
                targetScorePlusEnable = false
            )
        }

        GameType.FULL_COURT -> {
            copy(
                quarter = 4,
                quarterMinusEnable = true,
                quarterPlusEnable = false,
                playTime = 10,
                playTimeMinusEnable = true,
                playTimePlusEnable = true,
                breakTime = 5,
                breakTimeMinusEnable = true,
                breakTimePlusEnable = true
            )
        }
    }

    val startGameEnable: Boolean get() = gameType != null && homeTeamPlayer.isNotEmpty() && awayTeamPlayer.isNotEmpty()
}
