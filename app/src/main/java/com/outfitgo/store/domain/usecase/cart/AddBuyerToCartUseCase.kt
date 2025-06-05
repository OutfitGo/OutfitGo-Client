package com.outfitgo.store.domain.usecase.cart

import com.outfitgo.store.domain.repository.cart.CartRepository
import javax.inject.Inject

class AddBuyerToCartUseCase @Inject constructor(private val cartRepository: CartRepository){
    suspend fun execute(cartId:String,customerAccessToken:String){
        cartRepository.addBuyerToCart(cartId,customerAccessToken)
    }
}