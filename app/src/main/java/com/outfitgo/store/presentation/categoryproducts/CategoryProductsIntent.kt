package com.outfitgo.store.presentation.categoryproducts

import com.outfitgo.store.core.util.enums.ProductType
import com.outfitgo.store.core.util.enums.SortOption
import com.outfitgo.store.domain.model.product.Product

sealed interface CategoryProductsIntent {
    data class GetProducts(val categoryHandle: String) : CategoryProductsIntent
    data class GoToProductDetails(val product: Product) : CategoryProductsIntent
    data class Search(val query: String) : CategoryProductsIntent
    data class ChangeProductType(val productType: ProductType) : CategoryProductsIntent
    data class ApplyFilterOptions(
        val priceRange: ClosedFloatingPointRange<Float>,
        val sortOption: SortOption
    ) : CategoryProductsIntent
    object ShowFilterBottomSheet : CategoryProductsIntent
    object NavigateUp : CategoryProductsIntent
}