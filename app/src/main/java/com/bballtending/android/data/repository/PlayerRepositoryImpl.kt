package com.bballtending.android.data.repository

import com.bballtending.android.data.local.dao.GamePlayerRelationDao
import com.bballtending.android.data.local.dao.PlayerDao
import com.bballtending.android.data.local.database.AppDatabase
import com.bballtending.android.data.local.entity.PlayerEntity
import com.bballtending.android.domain.game.repository.PlayerRepository
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.domain.player.model.Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : PlayerRepository {

    private val playerDao: PlayerDao = appDatabase.playerDao()
    private val gamePlayerRelationDao: GamePlayerRelationDao = appDatabase.gamePlayerRelationDao()

    override suspend fun addPlayer(
        name: String,
        number: String,
        position: Position
    ): PlayerData {
        return withContext(Dispatchers.IO) {
            val playerEntity = playerDao.findPlayer(name, number)
            if (playerEntity != null) {
                PlayerData(
                    playerId = playerEntity.playerId,
                    name = playerEntity.name,
                    number = playerEntity.number,
                    position = position
                )
            } else {
                val newId = playerDao.insert(PlayerEntity(0, name, number))
                val newPlayerEntity = playerDao.findPlayerWithPlayerId(playerId = newId)
                PlayerData(
                    playerId = newPlayerEntity.playerId,
                    name = newPlayerEntity.name,
                    number = newPlayerEntity.number,
                    position = position
                )
            }
        }
    }
}