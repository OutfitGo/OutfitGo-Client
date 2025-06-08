package com.outfitgo.store.data.datasource.remote.user

import com.outfitgo.store.domain.model.User

interface UserRemoteDataSource {
    suspend fun loginByEmailAndPassword(email: String, password: String): LoginResponse
    suspend fun getUserByAccessToken(token: String): User?
    suspend fun registerNewUser(firstName: String, lastName: String, email: String, password: String): User
}

data class LoginResponse(val token: String, val errors: List<String>)