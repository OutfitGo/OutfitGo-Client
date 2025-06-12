package com.outfitgo.store.domain.repository.wishilst

import com.outfitgo.store.domain.model.product.Product

interface WishlistRepository {

    suspend fun addProduct(userId: String, product: Product)
    suspend fun removeProduct(userId: String, productId: String)
    suspend fun getAllProducts(userId: String): List<Product>
    suspend fun isInWishlist(userId: String, productId: String): Boolean

}