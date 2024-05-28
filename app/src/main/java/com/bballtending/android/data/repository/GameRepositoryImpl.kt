package com.bballtending.android.data.repository

import com.bballtending.android.TestModule
import com.bballtending.android.common.util.DLog
import com.bballtending.android.data.local.dao.GameDao
import com.bballtending.android.data.local.dao.GamePlayerRelationDao
import com.bballtending.android.data.local.database.AppDatabase
import com.bballtending.android.data.local.entity.GameEntity
import com.bballtending.android.data.local.entity.GamePlayerRelationEntity
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameDate
import com.bballtending.android.domain.game.model.GameType
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

    override suspend fun requestGameDataWithMonth(
        year: Int,
        month: Int
    ): Map<GameDate, List<GameData>> {
        return withContext(Dispatchers.IO) {
            val gameEntityList: List<GameEntity> =
                gameDao.findGameWithYearAndMonth(year, month)
            DLog.d(TAG, "gameEntityList.size()=${gameEntityList.size}")

            val ret = hashMapOf<GameDate, ArrayList<GameData>>()
            gameEntityList.forEach { gameEntity ->
                val relation = gamePlayerRelationDao.findRelationWithGameId(gameEntity.gameId)
                val homeTeamPlayer = arrayListOf<PlayerData>()
                val awayTeamPlayer = arrayListOf<PlayerData>()

                relation.forEach { (relationEntity, playerEntity) ->
                    val playerData = PlayerData(
                        playerId = playerEntity.playerId,
                        name = playerEntity.name,
                        number = playerEntity.number,
                        position = relationEntity.position,
                        score = relationEntity.score,
                        fieldGoalAttempt = relationEntity.twoPointAttempt + relationEntity.threePointAttempt,
                        fieldGoalSuccess = relationEntity.twoPointSuccess + relationEntity.threePointSuccess,
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

                val homeTeamScoreByQuarter = listOf(
                    gameEntity.homeTeamScore1.let { if (it > 0) it else 0 },
                    gameEntity.homeTeamScore2.let { if (it > 0) it else 0 },
                    gameEntity.homeTeamScore3.let { if (it > 0) it else 0 },
                    gameEntity.homeTeamScore4.let { if (it > 0) it else 0 }
                )
                val awayTeamScoreByQuarter = listOf(
                    gameEntity.awayTeamScore1.let { if (it > 0) it else 0 },
                    gameEntity.awayTeamScore2.let { if (it > 0) it else 0 },
                    gameEntity.awayTeamScore3.let { if (it > 0) it else 0 },
                    gameEntity.awayTeamScore4.let { if (it > 0) it else 0 }
                )

                val gameDate = GameDate(gameEntity.year, gameEntity.month, gameEntity.day)
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
                    homeTeamTotalScore = homeTeamScoreByQuarter.sum(),
                    awayTeamTotalScore = awayTeamScoreByQuarter.sum(),
                    homeTeamScoreByQuarter = homeTeamScoreByQuarter,
                    awayTeamScoreByQuarter = awayTeamScoreByQuarter,
                    homeTeamPlayer = homeTeamPlayer,
                    awayTeamPlayer = awayTeamPlayer
                )
                if (ret.containsKey(gameDate)) {
                    ret[gameDate]?.add(gameData)
                } else {
                    ret[gameDate] = arrayListOf(gameData)
                }

            }

            val testData = TestModule.createTestData()
            val testGameDate = GameDate(testData.year, testData.month, testData.day)
            ret[testGameDate] = arrayListOf(testData)

            ret
        }
    }

    override suspend fun createGame(
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
    ): GameData {
        return withContext(Dispatchers.IO) {
            val gameEntity = GameEntity(
                gameId = 0,
                year = year,
                month = month,
                day = day,
                hour = hour,
                minute = minute,
                gameType = gameType,
                quarter = quarter,
                playTime = playTime,
                breakTime = breakTime,
                homeTeamName = "홈 팀",
                awayTeamName = "어웨이 팀",
                homeTeamScore1 = 0,
                homeTeamScore2 = 0,
                homeTeamScore3 = 0,
                homeTeamScore4 = 0,
                awayTeamScore1 = 0,
                awayTeamScore2 = 0,
                awayTeamScore3 = 0,
                awayTeamScore4 = 0
            )
            val gameId = gameDao.insert(gameEntity)
            val newGameEntity = gameDao.findGameWithGameId(gameId)

            for (player in homeTeamPlayer) {
                val relationEntity = GamePlayerRelationEntity(
                    relationId = 0,
                    gameId = gameId,
                    playerId = player.playerId,
                    position = player.position,
                    isHomeTeamPlayer = true,
                    score = 0,
                    twoPointAttempt = 0,
                    twoPointSuccess = 0,
                    threePointAttempt = 0,
                    threePointSuccess = 0,
                    rebound = 0,
                    assist = 0,
                    steal = 0,
                    block = 0,
                    turnOver = 0,
                    foul = 0
                )
                gamePlayerRelationDao.insert(relationEntity)
            }

            for (player in awayTeamPlayer) {
                val relationEntity = GamePlayerRelationEntity(
                    relationId = 0,
                    gameId = gameId,
                    playerId = player.playerId,
                    position = player.position,
                    isHomeTeamPlayer = false,
                    score = 0,
                    twoPointAttempt = 0,
                    twoPointSuccess = 0,
                    threePointAttempt = 0,
                    threePointSuccess = 0,
                    rebound = 0,
                    assist = 0,
                    steal = 0,
                    block = 0,
                    turnOver = 0,
                    foul = 0
                )
                gamePlayerRelationDao.insert(relationEntity)
            }

            val homeTeamScoreByQuarter = listOf(
                newGameEntity.homeTeamScore1,
                newGameEntity.homeTeamScore2,
                newGameEntity.homeTeamScore3,
                newGameEntity.homeTeamScore4
            )
            val awayTeamScoreByQuarter = listOf(
                newGameEntity.awayTeamScore1,
                newGameEntity.awayTeamScore2,
                newGameEntity.awayTeamScore3,
                newGameEntity.awayTeamScore4
            )
            GameData(
                gameId = newGameEntity.gameId,
                year = newGameEntity.year,
                month = newGameEntity.month,
                day = newGameEntity.day,
                hour = newGameEntity.hour,
                minute = newGameEntity.minute,
                gameType = newGameEntity.gameType,
                quarter = newGameEntity.quarter,
                playTime = newGameEntity.playTime,
                breakTime = newGameEntity.breakTime,
                homeTeamName = newGameEntity.homeTeamName,
                awayTeamName = newGameEntity.awayTeamName,
                homeTeamTotalScore = homeTeamScoreByQuarter.sum(),
                awayTeamTotalScore = awayTeamScoreByQuarter.sum(),
                homeTeamScoreByQuarter = homeTeamScoreByQuarter,
                awayTeamScoreByQuarter = awayTeamScoreByQuarter,
                homeTeamPlayer = homeTeamPlayer,
                awayTeamPlayer = awayTeamPlayer
            )
        }
    }

    companion object {
        private const val TAG: String = "GameRepositryImpl"
    }
}