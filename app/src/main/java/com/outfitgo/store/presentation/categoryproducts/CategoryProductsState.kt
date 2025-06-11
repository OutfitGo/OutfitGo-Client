package com.outfitgo.store.presentation.categoryproducts

import com.outfitgo.store.core.util.enums.ProductType
import com.outfitgo.store.core.util.enums.SortOption
import com.outfitgo.store.domain.model.FilterOptions
import com.outfitgo.store.domain.model.product.Product

data class CategoryProductsState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val initialPriceRange: ClosedFloatingPointRange<Float> = 0f..0f,
    val filterOptions: FilterOptions = FilterOptions(
        searchQuery = "",
        productType = ProductType.ALL,
        priceRange = 0f..0f,
        sortOption = SortOption.ALPHABETICAL
    )
)
