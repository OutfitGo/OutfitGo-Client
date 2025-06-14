package com.outfitgo.store.domain.usecase.auth

import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class SendVerificationEmailUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend fun execute(email: String, password: String) {
        usersRepository.sendVerificationEmail(email, password)
    }

}