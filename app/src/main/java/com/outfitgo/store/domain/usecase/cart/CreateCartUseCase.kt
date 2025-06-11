package com.outfitgo.store.domain.usecase.cart

import com.outfitgo.store.domain.repository.cart.CartRepository
import javax.inject.Inject

class CreateCartUseCase @Inject constructor(private val cartRepository: CartRepository){
    suspend fun execute(): String {
        return cartRepository.createCart() // I return the cart here to save the cartId
    }
}