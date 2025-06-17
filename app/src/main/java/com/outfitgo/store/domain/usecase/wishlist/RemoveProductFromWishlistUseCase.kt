package com.outfitgo.store.domain.usecase.wishlist

import com.outfitgo.store.domain.repository.wishilst.WishlistRepository
import javax.inject.Inject


class RemoveProductFromWishlistUseCase  @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {

    suspend fun execute(userId: String, productId: String) {
        wishlistRepository.removeProduct(userId, productId)
    }
}