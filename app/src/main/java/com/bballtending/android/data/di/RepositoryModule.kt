package com.bballtending.android.data.di

import com.bballtending.android.data.repository.LoginRepositoryImpl
import com.bballtending.android.data.repository.NetworkRepository
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

}