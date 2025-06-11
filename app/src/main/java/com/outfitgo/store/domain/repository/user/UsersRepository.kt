package com.outfitgo.store.domain.repository.user

import com.outfitgo.store.domain.model.User

interface UsersRepository {
    // remote
    suspend fun loginWithEmailAndPassword(email: String, password: String): User?
    suspend fun getUserByToken(token: String): User?
    suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): User?


    // local
    suspend fun getSavedUserToken(): String?
    suspend fun getSavedUserId(): String?
    suspend fun saveUserId(userId: String)
    suspend fun saveToken(token: String)
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean

}
