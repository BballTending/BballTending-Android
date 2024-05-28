package com.bballtending.android.domain.game.usecase

import com.bballtending.android.domain.game.repository.ValidateGameDataRepository
import javax.inject.Inject

class ChangeTargetScoreUseCase @Inject constructor(
    private val repository: ValidateGameDataRepository
) {
    operator fun invoke(targetScore: Int): Boolean {
        return repository.validateTargetScore(targetScore)
    }
}