package com.outfitgo.store.presentation.brandproducts

import com.outfitgo.store.domain.model.product.CommonProduct

sealed interface BrandProductsIntent {
    object GoBack : BrandProductsIntent
    object GetNextProducts : BrandProductsIntent
    data class GoToProductDetails(val product: CommonProduct) : BrandProductsIntent
    data class ChangeSearchQuery(val query: String) : BrandProductsIntent
}