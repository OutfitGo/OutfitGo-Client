package com.outfitgo.store.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.usecase.products.SearchProductByTitleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductByTitleUseCase: SearchProductByTitleUseCase
): ViewModel() {
    private val _state = MutableStateFlow(SearchScreenUiState())
    val state = _state.asStateFlow()

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState = _searchState.asStateFlow()

    // to handle result of searching when user decreases the price
    private var originalProducts: List<Product> = emptyList()

    init {
        viewModelScope.launch {
            _searchState
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest {
                    searchByTitle(it.searchTitle)
                }
        }
    }


    fun processIntent(intent: SearchScreenIntent) {
        when(intent) {
            is SearchScreenIntent.MaxPriceChanged -> {
                _searchState.update { it.copy(maxPrice = intent.newMax) }
            }
            is SearchScreenIntent.SearchTitleChanged -> {
                _searchState.update { it.copy(searchTitle = intent.newTitle) }
            }
            is SearchScreenIntent.FilterProductsByPrice -> filterProductsByPrice(intent.price)
            is SearchScreenIntent.ChangeCurrentPrice -> {
                _searchState.update { it.copy(currentPrice = intent.currentPrice) }
            }
            else -> Unit
        }
    }

    private fun searchByTitle(title: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val products = searchProductByTitleUseCase.execute(title)
                val maxPrice = products.maxOf { it.price.toDouble() } + 50
                _searchState.update { it.copy(maxPrice = maxPrice) }

                originalProducts = products
                val filteredProducts = originalProducts.filter {
                    it.price.toDouble() <= _searchState.value.maxPrice
                            &&
                            it.price.toDouble() > _searchState.value.currentPrice.toDouble()
                }
                _state.update { it.copy(products = filteredProducts, isLoading = false) }
            } catch (exp: Exception) {
                _state.update { it.copy(products = emptyList(), isLoading = false) }
                Log.e(TAG, "searchByTitle: error in search", exp)
            }

        }
    }

    private fun filterProductsByPrice(price: Double) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val filteredProducts = originalProducts.filter {
                    it.price.toDouble() >= price
                }
                _state.update { it.copy(products = filteredProducts, isLoading = false) }
            } catch (exp: Exception) {
                _state.update { it.copy(products = emptyList(), isLoading = false) }
                Log.e(TAG, "searchByTitle: error in search", exp)
            }

        }
    }


}
