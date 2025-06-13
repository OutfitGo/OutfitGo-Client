package com.outfitgo.store.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.usecase.products.SearchProductByTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductByTitleUseCase: SearchProductByTitleUseCase
): ViewModel() {
    private val _state = MutableStateFlow(SearchScreenUiState())
    val state = _state.asStateFlow()

    private val _queryState = MutableStateFlow("")

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState = _searchState.asStateFlow()

    // to handle result of searching when user decreases the price
    private var originalProducts: List<Product> = emptyList()

    init {
        viewModelScope.launch {
            _queryState
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest {
                    searchByTitle(it)
                }
        }
    }


    fun processIntent(intent: SearchScreenIntent) {
        when(intent) {
            is SearchScreenIntent.SearchRangeChanged -> {
                _searchState.update { it.copy(range = intent.newRange) }
            }
            is SearchScreenIntent.SearchTitleChanged -> {
                _queryState.update { intent.newTitle }
            }

            is SearchScreenIntent.FilterProductsByRange -> filterProductsByRange(intent.range)
            else -> Unit
        }
    }

    private fun filterProductsByRange(range: ClosedFloatingPointRange<Float>) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val filteredProducts = originalProducts.filter {
                        it.price.toFloat() in range
                    }
                    _state.update { it.copy(products = filteredProducts, isLoading = false) }
                }
            } catch (exp: Exception) {
                _state.update { it.copy(products = emptyList(), isLoading = false) }
                Log.e(TAG, "searchByTitle: error in search", exp)
            }

        }
    }

    private fun searchByTitle(title: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val products = searchProductByTitleUseCase.execute(title)
                val maxPrice = products.maxOf { it.price.toFloat() } + 20
                var minPrice = products.minOf { it.price.toFloat() } - 20
                if (minPrice < 0) minPrice = 0f
                _searchState.update {
                    it.copy(
                        maxRange = (minPrice..maxPrice),
                        range = (minPrice..maxPrice),
                    )
                }

                originalProducts = products
                val filteredProducts = originalProducts.filter {
                    it.price.toFloat() in _searchState.value.range
                }
                _state.update { it.copy(products = filteredProducts, isLoading = false) }
            } catch (exp: Exception) {
                _state.update { it.copy(products = emptyList(), isLoading = false) }
                Log.e(TAG, "searchByTitle: error in search", exp)
            }

        }
    }

}
