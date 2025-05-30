package com.outfitgo.store.domain.usecase.auth

import com.outfitgo.store.domain.model.User
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class LoginWithEmailAndPasswordUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(email: String, password: String): User? {
        return usersRepository.loginWithEmailAndPassword(email, password)
    }

}