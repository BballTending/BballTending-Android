package com.bballtending.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bballtending.android.data.local.entity.PlayerEntity

@Dao
interface PlayerDao {
    @Insert
    suspend fun insert(playerEntity: PlayerEntity)

    @Delete
    suspend fun delete(playerEntity: PlayerEntity)

    @Query("SELECT * FROM player_table")
    suspend fun getAll(): List<PlayerEntity>

    @Query(
        "SELECT player_table.* FROM player_table " +
                "INNER JOIN game_player_relation_table ON game_player_relation_table.player_id = player_table.player_id " +
                "WHERE game_player_relation_table.game_id = :gameId"
    )
    suspend fun findPlayerWithGameId(gameId: Long): List<PlayerEntity>
}