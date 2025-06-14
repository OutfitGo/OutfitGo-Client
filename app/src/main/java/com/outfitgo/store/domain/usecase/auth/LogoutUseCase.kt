package com.outfitgo.store.domain.usecase.auth

import com.outfitgo.store.domain.repository.cart.CartRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
    private val cartRepository: CartRepository
) {

    suspend fun execute() {
        usersRepository.logout()
        cartRepository.saveCartId("")
    }
}