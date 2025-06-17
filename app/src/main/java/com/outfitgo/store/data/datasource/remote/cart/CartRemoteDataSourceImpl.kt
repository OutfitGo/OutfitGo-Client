package com.outfitgo.store.data.datasource.remote.cart

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.data.mappers.toDomain
import com.outfitgo.store.domain.model.cart.Cart
import com.outfitgo.store.domain.model.cart.Cost
import com.outfitgo.store.storefront.AddBuyerToCartMutation
import com.outfitgo.store.storefront.AddItemToCartMutation
import com.outfitgo.store.storefront.ApplyCouponToCartMutation
import com.outfitgo.store.storefront.CreateCartMutation
import com.outfitgo.store.storefront.GetCartQuery
import com.outfitgo.store.storefront.RemoveItemFromCartMutation
import com.outfitgo.store.storefront.UpdateCartLineQuantityMutation
import javax.inject.Inject

class CartRemoteDataSourceImpl @Inject constructor(
    private val remoteClient: ApolloClient
) : CartRemoteDataSource {
    override suspend fun getCart(cartId: String): Cart {
        val response = remoteClient.query(GetCartQuery(cartId)).execute()
        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }
        val data = response.data

        if (data?.cart == null) {
            throw Exception("Failed to Get Cart")
        }

        return data.cart.toDomain()
    }

    override suspend fun createCart(): String {
        val response = remoteClient.mutation(CreateCartMutation()).execute()
        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }
        val data = response.data

        if (data?.cartCreate == null) {
            throw Exception("Failed to Get Cart")
        }

        return data.cartCreate.cart?.id ?: ""
    }

    override suspend fun addBuyerToCart(
        cartId: String,
        customerAccessToken: String
    ): Boolean {
        val response =
            remoteClient.mutation(AddBuyerToCartMutation(cartId, customerAccessToken)).execute()
        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }
        return true
    }

    override suspend fun addItemToCart(
        cartId: String,
        quantity: Int,
        productVariantId: String
    ): Cost {
        val response =
            remoteClient.mutation(AddItemToCartMutation(cartId, quantity, productVariantId))
                .execute()
        Log.d("``TAG``", "addItemToCartResponse :${response.errors?.get(0)?.message} ")
        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }
        val data = response.data
        if (data?.cartLinesAdd?.cart == null) {
            throw Exception("Failed to Get Cart")
        }

        return data.cartLinesAdd.cart.toDomain()
    }

    override suspend fun removeItemFromCart(cartId: String, lineId: String): Cost {
        val response =
            remoteClient.mutation(RemoveItemFromCartMutation(cartId, lineId))
                .execute()
        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }
        val data = response.data
        if (data?.cartLinesRemove?.cart == null) {
            throw Exception("Failed to Get Cart")
        }

        return data.cartLinesRemove.cart.toDomain()
    }

    override suspend fun updateCartLineQuantity(
        cartId: String,
        lineId: String,
        quantity: Int
    ): Cost {
        val response =
            remoteClient.mutation(UpdateCartLineQuantityMutation(cartId, lineId, quantity))
                .execute()
        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }
        val data = response.data
        if (data?.cartLinesUpdate?.cart == null) {
            throw Exception("Failed to Get Cart")
        }
        // only it will update the cost
        return data.cartLinesUpdate.cart.toDomain()
    }

    override suspend fun applyCouponToCart(cartId: String, coupon: String): Cart {
        val response =
            remoteClient.mutation(ApplyCouponToCartMutation(cartId, coupon))
                .execute()
        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }
        val data = response.data

        if (data?.cartDiscountCodesUpdate?.cart == null) {
            throw Exception("Failed to Get Cart")
        }

        return data.cartDiscountCodesUpdate.cart.toDomain()
    }

}