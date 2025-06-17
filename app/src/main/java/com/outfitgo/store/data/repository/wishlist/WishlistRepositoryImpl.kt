package com.outfitgo.store.data.repository.wishlist

import android.util.Log
import com.outfitgo.store.data.datasource.remote.wishlist.WishlistRemoteDataSource
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.wishilst.WishlistRepository
import javax.inject.Inject


class WishlistRepositoryImpl @Inject constructor (
    private val remoteDataSource: WishlistRemoteDataSource
): WishlistRepository {
    override suspend fun addProduct(
        userId: String,
        product: Product
    ) {

        remoteDataSource.addProduct(userId, product)
    }

    override suspend fun removeProduct(userId: String, productId: String) {
        remoteDataSource.removeProduct(userId, productId)
    }

    override suspend fun getAllProducts(userId: String): List<Product> {
        return remoteDataSource.getAllProducts(userId)
    }

    override suspend fun isInWishlist(
        userId: String,
        productId: String
    ): Boolean {
        return remoteDataSource.isInWishlist(userId, productId)
    }
}