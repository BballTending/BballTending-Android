package com.bballtending.android.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bballtending.android.data.local.dao.GameDao
import com.bballtending.android.data.local.dao.GamePlayerRelationDao
import com.bballtending.android.data.local.dao.PlayerDao
import com.bballtending.android.data.local.entity.GameEntity
import com.bballtending.android.data.local.entity.GamePlayerRelationEntity
import com.bballtending.android.data.local.entity.PlayerEntity

@Database(
    entities = [GameEntity::class, PlayerEntity::class, GamePlayerRelationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    abstract fun playerDao(): PlayerDao

    abstract fun gamePlayerRelationDao(): GamePlayerRelationDao

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