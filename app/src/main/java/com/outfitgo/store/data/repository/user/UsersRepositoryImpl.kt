package com.outfitgo.store.data.repository.user

import android.util.Log
import com.outfitgo.store.data.datasource.remote.user.UserRemoteDataSource
import com.outfitgo.store.domain.model.User
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

private const val TAG = "UsersRepositoryImpl"

class UsersRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource
): UsersRepository {
    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): User? {
        Log.d(TAG, "loginWithEmailAndPassword: started")
        val loginResponse = remoteDataSource.loginByEmailAndPassword(email = email, password = password)
        return getUserByToken(token = loginResponse.token)
    }

    override suspend fun getUserByToken(token: String): User? {
        Log.i(TAG, "getUserByToken: started")
        return remoteDataSource.getUserByAccessToken(token)
    }

    override suspend fun getCurrentUserToken(): String {
        TODO("Not yet implemented")
    }

    override suspend fun saveToken(token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun isLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }
}

