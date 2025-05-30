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


// At the top level of your kotlin file:
val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class UserLocalDataSourceImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserLocalDataSource {

    companion object {
        const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY"
        const val USER_ID_KEY = "USER_ID_KEY"
    }

    override suspend fun saveUserToken(token: String) {
        withContext(dispatcher) {
            context.userDataStore.edit { settings ->
                settings[stringPreferencesKey(ACCESS_TOKEN_KEY)] = token
            }
        }
    }

    /**
     * returns null if no saved token else will return the token value
     * */
    override suspend fun getSavedUserToken(): String? =
        withContext(dispatcher) {
            context.userDataStore.data.map { settings ->
                settings[stringPreferencesKey(ACCESS_TOKEN_KEY)]
            }.first()
        }


    override suspend fun saveUserId(id: String) {
        withContext(dispatcher) {
            context.userDataStore.edit { settings ->
                settings[stringPreferencesKey(USER_ID_KEY)] = id
            }
        }
    }

    /**
     * returns null if no saved user id else will return the user id value
     * */
    override suspend fun getSavedUserId(): String? =
        withContext(dispatcher) {
            context.userDataStore.data.map { settings ->
                settings[stringPreferencesKey(USER_ID_KEY)]
            }.first()
        }


    override suspend fun isLoggedIn(): Boolean {
        val token = this.getSavedUserToken()
        return token != null
    }

    override suspend fun logout() {
        withContext(dispatcher) {
            context.userDataStore.edit { settings ->
                settings.clear()
            }
        }
    }

}