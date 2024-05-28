package com.bballtending.android.domain.game.usecase

import com.bballtending.android.domain.game.repository.ValidateGameDataRepository
import javax.inject.Inject

class ChangePlayTimeUseCase @Inject constructor(
    private val repository: ValidateGameDataRepository
) {
    operator fun invoke(playTime: Int): Boolean {
        return repository.validatePlayTime(playTime)
    }
}