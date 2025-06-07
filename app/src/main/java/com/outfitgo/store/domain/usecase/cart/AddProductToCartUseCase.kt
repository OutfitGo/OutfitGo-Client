package com.outfitgo.store.domain.usecase.cart

import com.outfitgo.store.domain.model.cart.Cost
import com.outfitgo.store.domain.repository.cart.CartRepository
import javax.inject.Inject

class AddProductToCartUseCase @Inject constructor(private val cartRepository: CartRepository){
    suspend fun execute(cartId:String,quantity:Int,productVariantId:String):Cost{
        return cartRepository.addItemToCart(cartId,quantity,productVariantId)
    }
}