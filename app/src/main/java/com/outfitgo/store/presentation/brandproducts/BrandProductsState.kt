package com.outfitgo.store.presentation.brandproducts

import com.outfitgo.store.domain.model.product.Product

data class BrandProductsState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val productsEndReached: Boolean = false,
    val productsLoadingError: String? = null
)