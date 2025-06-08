package com.outfitgo.store.domain.usecase.cart

import com.outfitgo.store.domain.model.cart.Cost
import com.outfitgo.store.domain.repository.cart.CartRepository
import javax.inject.Inject

class UpdateCartLineQuantityUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend fun execute(cartId: String, lineId: String, quantity: Int): Cost {
        return cartRepository.updateCartLineQuantity(cartId, lineId, quantity)
    }
}