package com.outfitgo.store.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.model.brand.Brand
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.domain.usecase.brands.GetBrandsUseCase
import com.outfitgo.store.domain.usecase.products.GetLatestProductsUseCase
import com.outfitgo.store.presentation.util.paging.DefaultPaginator
import com.outfitgo.store.presentation.util.paging.Paginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBrandsUseCase: GetBrandsUseCase,
    private val getLatestProductsUseCase: GetLatestProductsUseCase
) : ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState())
    val homeState = _homeState.asStateFlow()

    private val brandsPaginator: Paginator<String?, Brand> = DefaultPaginator(
        initialKey = null,
        isEndReached = {brands ->
            brands.isEmpty()
        },
        getNextKey = { brands ->
            brands.last().pageCursor
        },
        onRequest = { nextKey ->
            getBrandsUseCase.execute(first = 10, after = nextKey)
        },
        onLoadUpdated = { isLoading ->
            _homeState.update {
                it.copy(isBrandsLoading = isLoading)
            }
        },
        onSuccess = { newBrands ->
            _homeState.update {
                it.copy(
                    brands = (homeState.value.brands + newBrands)
                        .filter { it.name != "Home page" }
                        .distinctBy { it.name.uppercase() },
                    brandEndReached = newBrands.isEmpty()
                )
            }
        },
        onError = { throwable ->
            _homeState.update {
                it.copy(brandsLoadingError = throwable?.message.toString())
            }
        }
    )

    private val latestProductsPaginator: Paginator<String?, CommonProduct> = DefaultPaginator(
        initialKey = null,
        isEndReached = { products ->
            products.isEmpty()
        },
        getNextKey = { products ->
            products.last().pageCursor
        },
        onRequest = { nextKey ->
            getLatestProductsUseCase.execute(first = 10, after = nextKey)
        },
        onLoadUpdated = { isLoading ->
            _homeState.update {
                it.copy(isLatestProductsLoading = isLoading)
            }
        },
        onSuccess = { newProducts ->
            _homeState.update {
                it.copy(
                    latestProducts = homeState.value.latestProducts + newProducts,
                    latestProductsEndReached = newProducts.isEmpty()
                )
            }
        },
        onError = { throwable ->
            _homeState.update {
                it.copy(latestProductsLoadingError = throwable?.message.toString())
            }
        }
    )

    init {
        getNextBrands()
        getNextLatestProducts()
    }

    fun getNextBrands() {
        viewModelScope.launch(Dispatchers.IO) {
            brandsPaginator.loadNextItems()
        }
    }

    fun getNextLatestProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            latestProductsPaginator.loadNextItems()
        }
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.GetNextBrands -> getNextBrands()
            is HomeIntent.GetNextLatestProducts -> getNextLatestProducts()
            else -> Unit
        }
    }
}