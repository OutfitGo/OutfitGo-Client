package com.outfitgo.store.domain.usecase.wishlist

import android.util.Log
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.wishilst.WishlistRepository
import javax.inject.Inject


class AddProductToWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {

    suspend fun execute(userId: String, product: Product) {
        wishlistRepository.addProduct(userId, product)
    }
}