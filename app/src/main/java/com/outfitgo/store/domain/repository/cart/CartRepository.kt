package com.outfitgo.store.domain.repository.cart

import com.outfitgo.store.domain.model.cart.Cart

interface CartRepository {
    suspend fun getCart(cartId:String): Cart
    suspend fun createCart():Cart
    suspend fun addBuyerToCart(cartId:String,customerAccessToken:String):Cart
    suspend fun addItemToCart(cartId:String,quantity:Int,productVariantId:String):Cart
    suspend fun removeItemFromCart(cartId:String,lineId:String):Cart
    suspend fun updateCartLineQuantity(cartId:String,lineId:String,quantity:Int):Cart
    suspend fun applyCouponToCart(cartId:String,coupon:String):Cart
}