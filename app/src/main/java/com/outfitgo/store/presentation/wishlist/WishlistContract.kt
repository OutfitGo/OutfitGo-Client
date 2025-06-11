package com.outfitgo.store.presentation.wishlist

import com.outfitgo.store.domain.model.product.Product

data class WishlistUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
)


sealed interface WishlistIntent {
    object GetAllWishlistProducts: WishlistIntent
    data class RemoveProduct(val product: Product): WishlistIntent
    data class AddProductToCart(val product: Product): WishlistIntent
    data class GoToProductDetails(val product: Product): WishlistIntent
    object NavigateUp: WishlistIntent
}

sealed interface WishlistEffect {
    data class SendSnackBar(val msg: String): WishlistEffect
}
