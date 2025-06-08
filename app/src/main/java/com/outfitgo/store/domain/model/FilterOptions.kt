package com.outfitgo.store.domain.model

import com.outfitgo.store.core.util.enums.ProductType
import com.outfitgo.store.core.util.enums.SortOption

data class FilterOptions(
    val searchQuery: String,
    val productType: ProductType,
    val priceRange: ClosedFloatingPointRange<Float>,
    val sortOption: SortOption
)
