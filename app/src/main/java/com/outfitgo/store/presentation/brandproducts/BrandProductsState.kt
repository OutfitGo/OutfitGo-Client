package com.outfitgo.store.presentation.brandproducts

import com.outfitgo.store.domain.model.product.CommonProduct

data class BrandProductsState(
    val isLoading: Boolean = false,
    val products: List<CommonProduct> = emptyList(),
    val productsEndReached: Boolean = false,
    val productsLoadingError: String? = null
)