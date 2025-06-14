package com.outfitgo.store.domain.usecase.profile

import com.outfitgo.store.core.util.exceptions.MissingUserTokenException
import com.outfitgo.store.domain.model.User
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class GetUserByAccessTokenUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend fun execute(token: String?): User? {
        if(token == null) {
            throw MissingUserTokenException()
        }
        return usersRepository.getUserByToken(token)
    }
}