package com.outfitgo.store.domain.usecase.cart

import com.outfitgo.store.domain.repository.cart.CartRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class AddBuyerToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val usersRepository: UsersRepository
) {
    suspend fun execute(cartId: String): Boolean {
        return cartRepository.addBuyerToCart( cartId,usersRepository.getSavedUserToken()?:"")
    }
}