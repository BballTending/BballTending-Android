package com.bballtending.android.domain.player.model

data class PlayerData(
    val playerId: Long,
    /**
     * 이름
     */
    val name: String,
    /**
     * 등번호
     */
    val number: String,
    /**
     * 포지션
     */
    val position: Position,
    /**
     * 본 게임 득점 기록
     */
    val score: Int,
    /**
     * 야투 시도 횟수
     */
    val fieldGoalAttempt: Int,
    /**
     * 야투 성공 횟수
     */
    val fieldGoalSuccess: Int,
    /**
     * 2점 슛 시도 횟수
     */
    val twoPointAttempt: Int,
    /**
     * 2점 슛 성공 횟수
     */
    val twoPointSuccess: Int,
    /**
     * 3점 슛 시도 횟수
     */
    val threePointAttempt: Int,
    /**
     * 3점 슛 성공 횟수
     */
    val threePointSuccess: Int,
    /**
     * 리바운드
     */
    val rebound: Int,
    /**
     * 어시스트
     */
    val assist: Int,
    /**
     * 스틸
     */
    val steal: Int,
    /**
     * 블록
     */
    val block: Int,
    /**
     * 턴오버
     */
    val turnOver: Int,
    /**
     * 파울
     */
    val foul: Int
) {
    val fieldGoalRatio: String
        get() = "$fieldGoalSuccess/$fieldGoalAttempt"

    val fieldGoalPercentage: Float
        get() = (1f * fieldGoalSuccess / fieldGoalAttempt).let {
            if (it.isNaN()) 0.0f else it * 100
        }

    val threePointRatio: String
        get() = "$threePointSuccess/$threePointAttempt"

    val threePointPercentage: Float
        get() = (1f * threePointSuccess / threePointAttempt).let {
            if (it.isNaN()) 0.0f else it * 100
        }
}