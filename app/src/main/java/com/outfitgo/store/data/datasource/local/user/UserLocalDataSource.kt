package com.outfitgo.store.data.datasource.local.user


interface UserLocalDataSource {
    suspend fun saveUserToken(token: String)
    suspend fun getSavedUserToken(): String?

    suspend fun saveUserId(id: String)
    suspend fun getSavedUserId(): String?

    suspend fun saveUserEmail(email: String)
    suspend fun getSavedUserEmail(): String?

    suspend fun isLoggedIn(): Boolean
    suspend fun logout()
}
