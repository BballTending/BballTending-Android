package com.bballtending.android.domain.game.repository

import com.bballtending.android.domain.game.model.GameData

interface GameRepository {
    suspend fun requestGameDataWithMonth(year: Int, month: Int): Map<Int, List<GameData>>
}