package com.outfitgo.store.domain.usecase.auth

import com.outfitgo.store.domain.model.User
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class RegisterNewUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend fun execute(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): User? {
        return usersRepository.registerUser(firstName, lastName, email, password)
    }
}