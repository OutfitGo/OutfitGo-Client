package com.outfitgo.store.domain.usecase.auth

import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class SaveUserTokenUseCase @Inject constructor (
    private val usersRepository: UsersRepository
) {
    suspend fun execute(token: String) = usersRepository.saveToken(token)
}