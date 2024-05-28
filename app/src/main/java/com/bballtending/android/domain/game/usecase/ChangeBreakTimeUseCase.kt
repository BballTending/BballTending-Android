package com.bballtending.android.domain.game.usecase

import com.bballtending.android.domain.game.repository.ValidateGameDataRepository
import javax.inject.Inject

class ChangeBreakTimeUseCase @Inject constructor(
    private val repository: ValidateGameDataRepository
) {
    operator fun invoke(breakTime: Int): Boolean {
        return repository.validateBreakTime(breakTime)
    }
}