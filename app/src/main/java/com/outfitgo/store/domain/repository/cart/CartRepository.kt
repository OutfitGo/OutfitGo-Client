package com.outfitgo.store.domain.repository.cart

import com.outfitgo.store.domain.model.cart.Cart
import com.outfitgo.store.domain.model.cart.Cost
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getCart(cartId:String): Cart
    suspend fun createCart(): String
    suspend fun addBuyerToCart(cartId:String,customerAccessToken:String): Boolean
    suspend fun addItemToCart(cartId:String,quantity:Int,productVariantId:String): Cost
    suspend fun removeItemFromCart(cartId:String,lineId:String): Cost
    suspend fun updateCartLineQuantity(cartId:String,lineId:String,quantity:Int): Cost
    suspend fun applyCouponToCart(cartId:String,coupon:String):Cart
    suspend fun saveCartId(cartId: String)
    suspend fun getCartId(): Flow<String>
}