package com.outfitgo.store.domain.usecase.cart

import com.outfitgo.store.domain.repository.cart.CartRepository
import javax.inject.Inject

class RemoveItemFromCartUseCase @Inject constructor(private val cartRepository: CartRepository){
    suspend fun execute(cartId:String,lineId:String){
        cartRepository.removeItemFromCart(cartId,lineId)
    }
}