package com.bballtending.android.data.di

import com.bballtending.android.data.local.database.AppDatabase
import com.bballtending.android.data.repository.GameRepositoryImpl
import com.bballtending.android.data.repository.LoginRepositoryImpl
import com.bballtending.android.data.repository.NetworkRepository
import com.bballtending.android.data.repository.PlayerRepositoryImpl
import com.bballtending.android.data.repository.ValidateGameDataRepositoryImpl
import com.bballtending.android.domain.game.repository.GameRepository
import com.bballtending.android.domain.player.repository.PlayerRepository
import com.bballtending.android.domain.game.repository.ValidateGameDataRepository
import com.bballtending.android.domain.login.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLoginRepository(
        networkRepository: NetworkRepository
    ): LoginRepository =
        LoginRepositoryImpl(networkRepository)

    @Provides
    @Singleton
    fun provideGameRepository(
        appDatabase: AppDatabase
    ): GameRepository =
        GameRepositoryImpl(appDatabase)

    @Provides
    @Singleton
    fun provideValidateGameDataRepository(): ValidateGameDataRepository =
        ValidateGameDataRepositoryImpl()

    @Provides
    @Singleton
    fun providePlayerRepository(
        appDatabase: AppDatabase
    ): PlayerRepository =
        PlayerRepositoryImpl(appDatabase)
}