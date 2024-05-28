package com.bballtending.android.domain.game.repository

import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDate
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.player.model.PlayerData

interface GameRepository {
    suspend fun requestGameDataWithMonth(year: Int, month: Int): Map<GameDate, List<GameData>>

    suspend fun createGame(
        gameType: GameType,
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        quarter: Int,
        playTime: Int,
        breakTime: Int,
        homeTeamPlayer: List<PlayerData>,
        awayTeamPlayer: List<PlayerData>
    ): GameData
}