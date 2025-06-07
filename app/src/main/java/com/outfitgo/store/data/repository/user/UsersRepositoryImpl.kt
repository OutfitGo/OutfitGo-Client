package com.outfitgo.store.data.repository.user

import android.util.Log
import com.outfitgo.store.data.datasource.local.user.UserLocalDataSource
import com.outfitgo.store.data.datasource.remote.user.UserRemoteDataSource
import com.outfitgo.store.domain.model.User
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

private const val TAG = "UsersRepositoryImpl"

class UsersRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource
): UsersRepository {
    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): User? {
        Log.d(TAG, "loginWithEmailAndPassword: started")
        val loginResponse = remoteDataSource.loginByEmailAndPassword(email = email, password = password)
        localDataSource.saveUserToken(loginResponse.token)
        val user = getUserByToken(loginResponse.token)
        localDataSource.saveUserId(user?.id ?: "NOT-FOUND")
        return user
    }

    override suspend fun getUserByToken(token: String): User? {
        Log.i(TAG, "getUserByToken: started")
        return remoteDataSource.getUserByAccessToken(token)
    }

    override suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): User? {
        val newUser = remoteDataSource.registerNewUser(firstName, lastName, email, password)
        localDataSource.saveUserId(newUser.id)

        val loginResponse = remoteDataSource.loginByEmailAndPassword(email, password)
        localDataSource.saveUserToken(loginResponse.token)
        return newUser
    }

    override suspend fun getSavedUserToken(): String? {
        return localDataSource.getSavedUserToken()
    }

    override suspend fun saveToken(token: String) {
        localDataSource.saveUserToken(token)
    }

    override suspend fun logout() {
        localDataSource.logout()
    }

    override suspend fun isLoggedIn(): Boolean {
        return localDataSource.isLoggedIn()
    }

    override suspend fun getSavedUserId(): String? {
        return localDataSource.getSavedUserId()
    }

    override suspend fun saveUserId(userId: String) {
        localDataSource.saveUserId(userId)
    }
}

