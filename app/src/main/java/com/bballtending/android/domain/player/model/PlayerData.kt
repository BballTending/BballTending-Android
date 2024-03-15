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
)