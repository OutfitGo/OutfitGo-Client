package com.outfitgo.store.presentation.categoryproducts

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.usecase.collections.GetCategoryProductsUseCase
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.ApplyFilterOptions
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.ChangeProductType
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.GetProducts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val getCategoryProductsUseCase: GetCategoryProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryProductsState())
    val uiState = _uiState.asStateFlow()

    private fun getProducts(categoryHandle: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val products = getCategoryProductsUseCase.execute(categoryHandle = categoryHandle)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        products = products,
                        initialPriceRange = getProductsPriceRange(products),
                        filterOptions = uiState.value.filterOptions.copy(
                            priceRange = getProductsPriceRange(
                                products
                            )
                        )
                    )
                }
            } catch (exception: Exception) {
                //TODO Handle Error
                Log.d("```TAG```", "getProducts: ${exception.stackTrace}")
            }
        }
    }

    fun processIntent(intent: CategoryProductsIntent) {
        when (intent) {
            is GetProducts -> getProducts(categoryHandle = intent.categoryHandle)
            is CategoryProductsIntent.Search -> {
                _uiState.update {
                    it.copy(filterOptions = it.filterOptions.copy(searchQuery = intent.query))
                }
            }

            is ChangeProductType -> {
                _uiState.update {
                    it.copy(filterOptions = it.filterOptions.copy(productType = intent.productType))
                }
            }

            is ApplyFilterOptions -> {
                _uiState.update {
                    it.copy(
                        filterOptions = it.filterOptions.copy(
                            priceRange = intent.priceRange,
                            sortOption = intent.sortOption
                        )
                    )
                }
            }

            else -> Unit
        }
    }

    private fun getProductsPriceRange(products: List<Product>): ClosedFloatingPointRange<Float> {
        val minPrice = products.minBy { it.price.toFloat() }.price.toFloat() - 10f
        val maxPrice = products.maxBy { it.price.toFloat() }.price.toFloat() + 10f
        return minPrice..maxPrice
    }
}