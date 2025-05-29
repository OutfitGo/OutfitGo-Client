package com.outfitgo.store.domain.repository.user

import com.outfitgo.store.domain.model.User

interface UsersRepository {
    suspend fun loginWithEmailAndPassword(email: String, password: String): User?
    suspend fun getUserByToken(token: String): User?
    suspend fun getCurrentUserToken(): String
    suspend fun saveToken(token: String)
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}
