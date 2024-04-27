package com.bballtending.android.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_table")
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "player_id")
    val playerId: Long,
    val name: String,
    val number: String
)