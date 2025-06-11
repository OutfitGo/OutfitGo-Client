package com.outfitgo.store.domain.usecase.cart

import com.outfitgo.store.domain.model.cart.Cart
import com.outfitgo.store.domain.repository.cart.CartRepository
import javax.inject.Inject

class ApplyCouponToCartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend fun execute(cartId:String,coupon:String): Cart {
        return cartRepository.applyCouponToCart(cartId,coupon)
    }
}