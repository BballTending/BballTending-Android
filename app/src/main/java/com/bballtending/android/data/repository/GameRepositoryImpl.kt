package com.bballtending.android.data.repository

import com.bballtending.android.data.local.dao.GameDao
import com.bballtending.android.data.local.dao.GamePlayerRelationDao
import com.bballtending.android.data.local.database.AppDatabase
import com.bballtending.android.data.local.entity.GameEntity
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.repository.GameRepository
import com.bballtending.android.domain.player.model.PlayerData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : GameRepository {

    private val gameDao: GameDao = appDatabase.gameDao()
    private val gamePlayerRelationDao: GamePlayerRelationDao = appDatabase.gamePlayerRelationDao()

    override suspend fun requestGameDataWithMonth(year: Int, month: Int): Map<Int, List<GameData>> {
        return withContext(Dispatchers.IO) {
            val gameEntityList: List<GameEntity> =
                gameDao.findGameWithYearAndMonth(year, month)

            val ret = hashMapOf<Int, ArrayList<GameData>>()
            gameEntityList.forEach { gameEntity ->
                val relation = gamePlayerRelationDao.findRelationWithGameId(gameEntity.gameId)
                val homeTeamPlayer = arrayListOf<PlayerData>()
                val awayTeamPlayer = arrayListOf<PlayerData>()

                relation.forEach { (relationEntity, playerEntity) ->
                    val playerData = PlayerData(
                        playerId = playerEntity.playerId,
                        name = playerEntity.name,
                        number = playerEntity.number,
                        position = playerEntity.position,
                        score = relationEntity.score,
                        twoPointAttempt = relationEntity.twoPointAttempt,
                        twoPointSuccess = relationEntity.twoPointSuccess,
                        threePointAttempt = relationEntity.threePointAttempt,
                        threePointSuccess = relationEntity.threePointSuccess,
                        rebound = relationEntity.rebound,
                        assist = relationEntity.assist,
                        steal = relationEntity.steal,
                        block = relationEntity.block,
                        turnOver = relationEntity.turnOver,
                        foul = relationEntity.foul
                    )

                    if (relationEntity.isHomeTeamPlayer) {
                        homeTeamPlayer.add(playerData)
                    } else {
                        awayTeamPlayer.add(playerData)
                    }
                }

                val day = gameEntity.day
                val gameData = GameData(
                    gameId = gameEntity.gameId,
                    year = gameEntity.year,
                    month = gameEntity.month,
                    day = gameEntity.day,
                    hour = gameEntity.hour,
                    minute = gameEntity.minute,
                    gameType = gameEntity.gameType,
                    quarter = gameEntity.quarter,
                    playTime = gameEntity.playTime,
                    breakTime = gameEntity.breakTime,
                    homeTeamName = gameEntity.homeTeamName,
                    awayTeamName = gameEntity.awayTeamName,
                    homeTeamScore = gameEntity.homeTeamScore,
                    awayTeamScore = gameEntity.awayTeamScore,
                    homeTeamPlayer = homeTeamPlayer,
                    awayTeamPlayer = awayTeamPlayer
                )
                if (ret.containsKey(day)) {
                    ret[day]?.add(gameData)
                } else {
                    ret[day] = arrayListOf(gameData)
                }
            }

            ret
        }
    }
}