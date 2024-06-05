package com.bballtending.android.domain.player.repository

import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.domain.player.model.Position

interface PlayerRepository {
    suspend fun addPlayer(
        name: String,
        number: String,
        position: Position
    ): PlayerData
}