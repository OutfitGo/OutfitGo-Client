package com.outfitgo.store.data.datasource.local.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class UserLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserLocalDataSource {

    companion object {
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
        private const val USER_ID_KEY = "USER_ID_KEY"
    }

    override suspend fun saveUserToken(token: String) {
        withContext(dispatcher) {
            dataStore.edit { settings ->
                settings[stringPreferencesKey(ACCESS_TOKEN_KEY)] = token
            }
        }
    }

    /**
     * returns null if no saved token else will return the token value
     * */
    override suspend fun getSavedUserToken(): String? =
        withContext(dispatcher) {
            dataStore.data.map { settings ->
                settings[stringPreferencesKey(ACCESS_TOKEN_KEY)]
            }.first()
        }


    override suspend fun saveUserId(id: String) {
        withContext(dispatcher) {
            dataStore.edit { settings ->
                settings[stringPreferencesKey(USER_ID_KEY)] = id
            }
        }
    }

    /**
     * returns null if no saved user id else will return the user id value
     * */
    override suspend fun getSavedUserId(): String? =
        withContext(dispatcher) {
            dataStore.data.map { settings ->
                settings[stringPreferencesKey(USER_ID_KEY)]
            }.first()
        }


    override suspend fun isLoggedIn(): Boolean {
        val token = this.getSavedUserToken()
        return token != null
    }

    override suspend fun logout() {
        withContext(dispatcher) {
            dataStore.edit { settings ->
                settings.remove(stringPreferencesKey(ACCESS_TOKEN_KEY))
                settings.remove(stringPreferencesKey(USER_ID_KEY))
            }
        }
    }

}