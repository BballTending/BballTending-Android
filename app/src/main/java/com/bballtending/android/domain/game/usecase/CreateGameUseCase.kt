package com.bballtending.android.domain.game.usecase

import com.bballtending.android.domain.NetworkResult
import com.bballtending.android.domain.game.model.GameData
import com.bballtending.android.domain.game.model.GameType
import com.bballtending.android.domain.game.repository.GameRepository
import com.bballtending.android.domain.player.model.PlayerData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateGameUseCase @Inject constructor(
    private val repository: GameRepository
) {
    suspend operator fun invoke(
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
    ): NetworkResult<GameData> {
        return withContext(Dispatchers.IO) {
            val createdGameData = repository.createGame(
                gameType,
                year,
                month,
                day,
                hour,
                minute,
                quarter,
                playTime,
                breakTime,
                homeTeamPlayer,
                awayTeamPlayer
            )
            NetworkResult.Success(createdGameData)
        }
    }
}