package com.bballtending.android.domain.game.usecase

import com.bballtending.android.domain.NetworkResult
import com.bballtending.android.domain.game.repository.PlayerRepository
import com.bballtending.android.domain.player.model.PlayerData
import com.bballtending.android.domain.player.model.Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddPlayerUseCase @Inject constructor(
    private val repository: PlayerRepository
) {
    suspend operator fun invoke(
        name: String,
        number: String,
        position: Position
    ): NetworkResult<PlayerData> {
        return withContext(Dispatchers.IO) {
            val addedPlayer = repository.addPlayer(name, number, position)
            NetworkResult.Success(addedPlayer)
        }
    }
}