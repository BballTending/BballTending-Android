package com.bballtending.android.data.local.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.bballtending.android.data.local.dao.GameDao
import com.bballtending.android.data.local.dao.GamePlayerRelationDao
import com.bballtending.android.data.local.dao.PlayerDao
import com.bballtending.android.data.local.entity.GameEntity
import com.bballtending.android.data.local.entity.GamePlayerRelationEntity
import com.bballtending.android.data.local.entity.PlayerEntity

@Database(
    entities = [GameEntity::class, PlayerEntity::class, GamePlayerRelationEntity::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = AppDatabase.AutoMigrationSpecFrom1to2::class)
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    abstract fun playerDao(): PlayerDao

    abstract fun gamePlayerRelationDao(): GamePlayerRelationDao

    /**
     * from 1 to 2 변경사항
     *
     * game_table 변경사항
     * 1. home_team_score 및 away_team_score 컬럼 삭제
     * 2. home_team_score_1 ~ home_team_score_4 컬럼 추가
     * 3. away_team_score_1 ~ away_team_score_4 컬럼 추가
     *
     * player_table 변경사항
     * 1. number 컬럼이 Int에서 String으로 변경됨
     */
    @DeleteColumn(tableName = "game_table", columnName = "home_team_score")
    @DeleteColumn(tableName = "game_table", columnName = "away_team_score")
    class AutoMigrationSpecFrom1to2 : AutoMigrationSpec

    companion object {
        const val DB_NAME: String = "BballTending_Database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        DB_NAME
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}