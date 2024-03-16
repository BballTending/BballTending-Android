package com.bballtending.android.domain.game.model

/**
 * 스코어 보드 정렬 타입
 *
 * @property DEFAULT 기본
 * @property SCORE 득점
 * @property REBOUND 리바운드
 * @property ASSIST 어시스트
 * @property STEAL 스틸
 * @property BLOCK 블록슛
 * @property FIELD_GOAL_RATIO 야투
 * @property FIELD_GOAL_PERCENTAGE 야투%
 * @property THREE_POINT_RATIO 3점슛
 * @property THREE_POINT_PERCENTAGE 3점슛%
 * @property FOUL 파울
 */
enum class SortType {
    DEFAULT,
    SCORE,
    REBOUND,
    ASSIST,
    STEAL,
    BLOCK,
    FIELD_GOAL_RATIO,
    FIELD_GOAL_PERCENTAGE,
    THREE_POINT_RATIO,
    THREE_POINT_PERCENTAGE,
    FOUL
}