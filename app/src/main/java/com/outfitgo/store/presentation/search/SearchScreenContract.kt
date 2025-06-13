package com.outfitgo.store.presentation.search

import com.outfitgo.store.domain.model.product.Product


data class SearchScreenUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
)
data class SearchUiState(
    val range: ClosedFloatingPointRange<Float> = (0f..100f),
    val maxRange: ClosedFloatingPointRange<Float> = (0f..100f),
)

sealed interface SearchScreenIntent {
    data class SearchTitleChanged(val newTitle: String): SearchScreenIntent
    data object GoBack: SearchScreenIntent
    data class GoToProductDetails(val productId: String): SearchScreenIntent

    data class SearchRangeChanged(val newRange: ClosedFloatingPointRange<Float>) :
        SearchScreenIntent

    data class FilterProductsByRange(val range: ClosedFloatingPointRange<Float>) :
        SearchScreenIntent
}
