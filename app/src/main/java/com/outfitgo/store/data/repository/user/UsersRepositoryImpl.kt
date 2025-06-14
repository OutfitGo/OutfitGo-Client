package com.outfitgo.store.data.repository.user

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.outfitgo.store.data.datasource.local.user.UserLocalDataSource
import com.outfitgo.store.data.datasource.remote.user.UserRemoteDataSource
import com.outfitgo.store.domain.model.User
import com.outfitgo.store.domain.repository.user.UsersRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "UsersRepositoryImpl"

class UsersRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource,
    private val firebaseAuth: FirebaseAuth
): UsersRepository {
    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): User? {
        Log.d(TAG, "loginWithEmailAndPassword: started")
        val loginResponse = remoteDataSource.loginByEmailAndPassword(email = email, password = password)
        localDataSource.saveUserToken(loginResponse.token)

        localDataSource.saveUserEmail(email)

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

        firebaseAuth.currentUser?.reload()?.await()
        val user = firebaseAuth.currentUser
        if (user != null) {
            if (user.isEmailVerified) {
                Log.i(TAG, "registerUser: user is verified")
                val newUser = remoteDataSource.registerNewUser(firstName, lastName, email, password)
                localDataSource.saveUserId(newUser.id)

                val loginResponse = remoteDataSource.loginByEmailAndPassword(email, password)
                localDataSource.saveUserToken(loginResponse.token)

                localDataSource.saveUserEmail(email)

                Log.i(TAG, "registerUser: user created successfully")
                return newUser
            } else {
                throw Exception("Please Verify your email first")
            }
        } else return null
    }

    override suspend fun sendVerificationEmail(email: String, password: String) {
        // create the user then send email
        if (firebaseAuth.currentUser == null) {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user
            Log.i(TAG, "sendVerificationEmail: user = $user")
        }
        firebaseAuth.currentUser?.sendEmailVerification()

    }


    override suspend fun getSavedUserToken(): String? {
        return localDataSource.getSavedUserToken()
    }

    override suspend fun saveToken(token: String) {
        localDataSource.saveUserToken(token)
    }

    override suspend fun logout() {
        localDataSource.logout()
        firebaseAuth.signOut()
    }

    override suspend fun isLoggedIn(): Boolean {
        return localDataSource.isLoggedIn()
    }

    override suspend fun getSavedUserEmail(): String? {
        return localDataSource.getSavedUserId()
    }

    override suspend fun getSavedUserId(): String? {
        return localDataSource.getSavedUserId()
    }

    override suspend fun saveUserId(userId: String) {
        localDataSource.saveUserId(userId)
    }
}

