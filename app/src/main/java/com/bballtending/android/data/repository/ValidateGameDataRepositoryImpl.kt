package com.bballtending.android.data.repository

import com.bballtending.android.domain.game.repository.ValidateGameDataRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidateGameDataRepositoryImpl @Inject constructor() : ValidateGameDataRepository {
    override fun validateQuarter(quarter: Int): Boolean {
        return quarter in MIN_QUARTER..MAX_QUARTER
    }

    override fun validatePlayTime(playTime: Int): Boolean {
        return playTime in MIN_PLAY_TIME..MAX_PLAY_TIME
    }

    override fun validateBreakTime(breakTime: Int): Boolean {
        return breakTime in MIN_BREAK_TIME..MAX_BREAK_TIME
    }

    companion object {
        private const val MIN_QUARTER: Int = 1
        private const val MAX_QUARTER: Int = 4

        private const val MIN_PLAY_TIME: Int = 1
        private const val MAX_PLAY_TIME: Int = 12

        private const val MIN_BREAK_TIME: Int = 0
        private const val MAX_BREAK_TIME: Int = 60
    }
}