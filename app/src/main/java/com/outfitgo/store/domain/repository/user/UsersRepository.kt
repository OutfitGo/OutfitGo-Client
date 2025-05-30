package com.outfitgo.store.domain.repository.user

import com.outfitgo.store.domain.model.User

interface UsersRepository {
    // remote
    suspend fun loginWithEmailAndPassword(email: String, password: String): User?
    // remote
    suspend fun getUserByToken(token: String): User?
    // local
    suspend fun getSavedUserToken(): String?
    // local
    suspend fun getSavedUserId(): String?

    // local
    suspend fun saveUserId(userId: String)
    // local
    suspend fun saveToken(token: String)
    // local
    suspend fun logout()
    // local
    suspend fun isLoggedIn(): Boolean

}
