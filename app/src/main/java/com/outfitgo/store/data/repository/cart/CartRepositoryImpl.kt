package com.outfitgo.store.data.repository.cart

import com.outfitgo.store.data.datasource.remote.cart.CartRemoteDataSource
import com.outfitgo.store.domain.model.cart.Cart
import com.outfitgo.store.domain.model.cart.Cost
import com.outfitgo.store.domain.repository.cart.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val remote: CartRemoteDataSource
) : CartRepository {
    override suspend fun getCart(cartId: String): Cart {
        return remote.getCart(cartId)
    }

    override suspend fun createCart(): String {
        return remote.createCart()
    }

    override suspend fun addBuyerToCart(cartId: String, customerAccessToken: String): Boolean {
        return remote.addBuyerToCart(cartId, customerAccessToken)
    }

    override suspend fun addItemToCart(
        cartId: String,
        quantity: Int,
        productVariantId: String
    ): Cost {
        return remote.addItemToCart(cartId, quantity, productVariantId)
    }

    override suspend fun removeItemFromCart(cartId: String, lineId: String): Cost {
        return remote.removeItemFromCart(cartId, lineId)
    }

    override suspend fun updateCartLineQuantity(
        cartId: String,
        lineId: String,
        quantity: Int
    ): Cost {
        return remote.updateCartLineQuantity(cartId, lineId, quantity)
    }

    override suspend fun applyCouponToCart(cartId: String, coupon: String): Cart {
        return remote.applyCouponToCart(cartId, coupon)
    }

    override suspend fun saveCartId(cartId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCartId(): String {
        TODO("Not yet implemented")
    }
}