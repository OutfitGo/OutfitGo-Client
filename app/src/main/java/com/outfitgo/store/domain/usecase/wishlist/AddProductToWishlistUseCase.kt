package com.outfitgo.store.domain.usecase.wishlist

import android.util.Log
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.wishilst.WishlistRepository
import javax.inject.Inject

private const val TAG = "AddProductToWishlistUse"

class AddProductToWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {

    suspend fun execute(userId: String, product: Product) {
        Log.i(TAG, "execute: started")
        wishlistRepository.addProduct(userId, product)
    }
}