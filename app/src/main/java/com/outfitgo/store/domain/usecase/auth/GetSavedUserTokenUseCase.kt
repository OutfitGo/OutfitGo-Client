package com.outfitgo.store.domain.usecase.auth

import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class GetSavedUserTokenUseCase @Inject constructor(
    private val usersRepository: UsersRepository
){
    suspend fun execute() = usersRepository.getSavedUserToken()
}

