package com.bballtending.android.domain.game.usecase

import com.bballtending.android.domain.game.repository.ValidateGameDataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangeQuarterUseCase @Inject constructor(
    private val repository: ValidateGameDataRepository
) {
    operator fun invoke(quarter: Int): Boolean {
        return repository.validateQuarter(quarter)
    }
}