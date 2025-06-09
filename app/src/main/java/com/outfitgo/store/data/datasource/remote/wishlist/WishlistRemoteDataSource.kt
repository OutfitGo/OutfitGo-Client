package com.outfitgo.store.data.datasource.remote.wishlist

import com.outfitgo.store.domain.model.product.Product

interface WishlistRemoteDataSource {

    suspend fun addProduct(userId: String, product: Product)
    suspend fun removeProduct(userId: String, productId: String)
    suspend fun getAllProducts(userId: String): List<Product>
    suspend fun isInWishlist(userId: String, productId: String): Boolean

}