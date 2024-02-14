package com.bballtending.android.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bballtending.android.domain.user.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val userPreferencesDataStore: DataStore<Preferences>
) : UserPreferencesRepository {
    override suspend fun getUserToken(): Result<String> {
        return Result.runCatching {
            val flow = userPreferencesDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences ->
                    preferences[KEY_USER_TOKEN]
                }
            flow.firstOrNull() ?: ""
        }
    }

    override suspend fun setUserToken(userToken: String) {
        Result.runCatching {
            userPreferencesDataStore.edit { preferences ->
                preferences[KEY_USER_TOKEN] = userToken
            }
        }
    }

    companion object {
        private const val TAG: String = "UserPreferencesRepositoryImpl"

        private val KEY_USER_TOKEN = stringPreferencesKey(
            name = "user_token"
        )
    }
}