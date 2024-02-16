package com.bballtending.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bballtending.android.data.local.entity.GamePlayerRelationEntity
import com.bballtending.android.data.local.entity.PlayerEntity

@Dao
interface GamePlayerRelationDao {
    @Insert
    suspend fun insert(gamePlayerRelationEntity: GamePlayerRelationEntity)

    @Delete
    suspend fun delete(gamePlayerRelationEntity: GamePlayerRelationEntity)

    @Query("SELECT * FROM game_player_relation_table")
    suspend fun getAll(): List<GamePlayerRelationEntity>

    @Query(
        "SELECT * FROM game_player_relation_table " +
                "INNER JOIN player_table ON player_table.player_id = game_player_relation_table.player_id " +
                "WHERE game_player_relation_table.game_id = :gameId"
    )
    suspend fun findRelationWithGameId(gameId: Long): Map<GamePlayerRelationEntity, List<PlayerEntity>>
}