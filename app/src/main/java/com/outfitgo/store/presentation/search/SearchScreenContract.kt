package com.outfitgo.store.presentation.search

import com.outfitgo.store.domain.model.product.CommonProduct

data class SearchScreenUiState(
    val products: List<CommonProduct> = emptyList(),
    val isLoading: Boolean = false,
)
data class SearchUiState(
    val searchTitle: String = "",
    val minPrice: Double = 0.0,
    val maxPrice: Double = 100.0
)

sealed interface SearchScreenIntent {
    data class SearchTitleChanged(val newTitle: String): SearchScreenIntent
    data class MaxPriceChanged(val newMax: Double): SearchScreenIntent
    data class FilterProductsByPrice(val price: Double): SearchScreenIntent
    data object GoBack: SearchScreenIntent
    data class GoToProductDetails(val productId: String): SearchScreenIntent
}
