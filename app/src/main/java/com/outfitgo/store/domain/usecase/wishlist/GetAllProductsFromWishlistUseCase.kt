package com.outfitgo.store.domain.usecase.wishlist

import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.wishilst.WishlistRepository
import javax.inject.Inject

class GetAllProductsFromWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {

    suspend fun execute(userId: String): List<Product> {
        return wishlistRepository.getAllProducts(userId)
    }
}