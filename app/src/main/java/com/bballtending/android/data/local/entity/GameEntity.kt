package com.bballtending.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bballtending.android.domain.game.model.GameType

@Entity(tableName = "game_table")
data class GameEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "game_id")
    val gameId: Long,
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    @ColumnInfo(name = "game_type")
    val gameType: GameType,
    val quarter: Int,
    @ColumnInfo(name = "play_time")
    val playTime: Int,
    @ColumnInfo(name = "break_time")
    val breakTime: Int,
    @ColumnInfo(name = "home_team_name")
    val homeTeamName: String,
    @ColumnInfo(name = "away_team_name")
    val awayTeamName: String,
    @ColumnInfo(name = "home_team_score")
    val homeTeamScore: Int,
    @ColumnInfo(name = "away_team_score")
    val awayTeamScore: Int
)
