package com.outfitgo.store.presentation.productdetails

import com.outfitgo.store.domain.model.product.DetailedProduct

data class ProductDetailsState(
    val product: DetailedProduct = DetailedProduct(
        id = "boody",
        title = "Product Title",
        description = "Product Description sdlkfsldfjsdlfjsldfjsldfj",
        price = "44.6",
        imageUrls = listOf(),
        tags = listOf(),
        vendor = "BOODY",
        category = "BOODY",
        rating = 2.9,
        reviews = listOf(),
        currencyCode = "EGP"
    ),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val isAddedToCart: Boolean = false
)

sealed interface ProductDetailsIntent{
    data class GetProductById(val productId: String): ProductDetailsIntent
    data class AddToWishlist(val productId: String): ProductDetailsIntent
    data class AddToCart(val productId: String): ProductDetailsIntent
}

sealed interface ProductDetailsEffect {
    data class SendSnackBar(val msg: String): ProductDetailsEffect
}