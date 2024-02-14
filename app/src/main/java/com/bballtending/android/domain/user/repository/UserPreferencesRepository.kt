package com.bballtending.android.domain.user.repository

interface UserPreferencesRepository {
    suspend fun getUserToken(): Result<String>

    suspend fun setUserToken(userToken: String)
}