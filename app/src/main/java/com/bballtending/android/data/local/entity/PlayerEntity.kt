package com.bballtending.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bballtending.android.domain.player.model.Position

@Entity(tableName = "player_table")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "player_id")
    val playerId: Long,
    val name: String,
    val number: Int,
    val position: Position
)