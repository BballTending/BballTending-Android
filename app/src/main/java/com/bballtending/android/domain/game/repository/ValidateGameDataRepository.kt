package com.bballtending.android.domain.game.repository

interface ValidateGameDataRepository {
    /**
     * 쿼터 수가 유효한 값인지 판단
     * @param quarter   게임 쿼터 수
     */
    fun validateQuarter(quarter: Int): Boolean

    /**
     * 쿼터 별 플레이 시간이 유효한 값인지 판단
     * @param playTime  쿼터 별 플레이 시간
     */
    fun validatePlayTime(playTime: Int): Boolean

    /**
     * 쉬는 시간이 유효한 값인지 판단
     * @param breakTime 쉬는 시간
     */
    fun validateBreakTime(breakTime: Int): Boolean

    /**
     * 목표 점수가 유효한 값인지 판단
     * @param targetScore 목표 점수
     */
    fun validateTargetScore(targetScore: Int): Boolean
}