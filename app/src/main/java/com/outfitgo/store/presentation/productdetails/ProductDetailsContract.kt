package com.outfitgo.store.presentation.productdetails

import com.outfitgo.store.domain.model.product.DetailedProduct
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.model.product.ProductVariant

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
        rating = 2,
        reviews = listOf(),
        currencyCode = "EGP",
        variants = emptyList()
    ),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val isAddedToCart: Boolean = false,
    val selectedVariantId: String = ""
)

sealed interface ProductDetailsIntent{
    data class GetProductById(val productId: String): ProductDetailsIntent
    data class AddToWishlist(val product: Product): ProductDetailsIntent
    data class RemoveFromWishList(val productId: String): ProductDetailsIntent
    data class AddToCart(val productVariantId: String): ProductDetailsIntent
    data class SelectProductVariant(val variant: ProductVariant): ProductDetailsIntent
}

sealed interface ProductDetailsEffect {
    data class SendSnackBar(val msg: String): ProductDetailsEffect
}