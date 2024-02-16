package com.bballtending.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 게임과 플레이어 사이의 관계를 의미하는 테이블 Entity
 */
@Entity(
    tableName = "game_player_relation_table",
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = arrayOf("game_id"),
            childColumns = arrayOf("game_id"),
            onDelete = ForeignKey.CASCADE
        ), ForeignKey(
            entity = PlayerEntity::class,
            parentColumns = arrayOf("player_id"),
            childColumns = arrayOf("player_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("game_id"),
        Index("player_id")
    ]
)
data class GamePlayerRelationEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "relation_id")
    val relationId: Long,
    /**
     * 게임 ID
     */
    @ColumnInfo(name = "game_id")
    val gameId: Long,
    /**
     * 플레이어 ID
     */
    @ColumnInfo(name = "player_id")
    val playerId: Long,
    /**
     * 홈 팀 선수 여부
     */
    @ColumnInfo(name = "is_home_team_player")
    val isHomeTeamPlayer: Boolean,
    /**
     * 본 게임 득점 기록
     */
    val score: Int,
    /**
     * 2점 슛 시도 횟수
     */
    @ColumnInfo(name = "two_point_attempt")
    val twoPointAttempt: Int,
    /**
     * 2점 슛 성공 횟수
     */
    @ColumnInfo(name = "two_point_success")
    val twoPointSuccess: Int,
    /**
     * 3점 슛 시도 횟수
     */
    @ColumnInfo(name = "three_point_attempt")
    val threePointAttempt: Int,
    /**
     * 3점 슛 성공 횟수
     */
    @ColumnInfo(name = "three_point_success")
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
    @ColumnInfo(name = "turn_over")
    val turnOver: Int,
    /**
     * 파울
     */
    val foul: Int
)
