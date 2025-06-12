package com.outfitgo.store.domain.usecase.cart

import com.outfitgo.store.domain.repository.cart.CartRepository
import javax.inject.Inject

class SaveCartIdUseCase @Inject constructor(private val repository: CartRepository) {
    suspend fun execute(cartId:String){
        repository.saveCartId(cartId)
    }
}