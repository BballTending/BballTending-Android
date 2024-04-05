package com.bballtending.android.domain.game.repository

import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDate

interface GameRepository {
    suspend fun requestGameDataWithMonth(year: Int, month: Int): Map<GameDate, List<GameData>>
}