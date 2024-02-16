package com.bballtending.android.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.bballtending.android.data.local.entity.GameEntity

@Dao
interface GameDao {
    @Insert
    suspend fun insert(gameEntity: GameEntity)

    @Delete
    suspend fun delete(gameEntity: GameEntity)

    @Query("SELECT * FROM game_table")
    suspend fun getAll(): List<GameEntity>

    @Query("SELECT * FROM game_table WHERE year = :year AND month = :month")
    suspend fun findGameWithYearAndMonth(year: Int, month: Int): List<GameEntity>
}