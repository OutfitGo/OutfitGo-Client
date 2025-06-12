package com.outfitgo.store.data.datasource.remote.cart

import com.outfitgo.store.domain.model.cart.Cart
import com.outfitgo.store.domain.model.cart.Cost

interface CartRemoteDataSource {
    suspend fun getCart(cartId:String): Cart
    suspend fun createCart(): String // return the cart id
    suspend fun addBuyerToCart(cartId:String,customerAccessToken:String): Boolean
    suspend fun addItemToCart(cartId:String,quantity:Int,productVariantId:String): Cost // only i care about the new cost
    suspend fun removeItemFromCart(cartId:String,lineId:String): Cost
    suspend fun updateCartLineQuantity(cartId:String,lineId:String,quantity:Int): Cost
    suspend fun applyCouponToCart(cartId:String,coupon:String): Cart // i return cart here to see if coupon applied and new cost
}