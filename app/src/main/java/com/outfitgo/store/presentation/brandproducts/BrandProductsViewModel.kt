package com.outfitgo.store.presentation.brandproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.domain.usecase.brands.GetBrandProductsUseCase
import com.outfitgo.store.presentation.util.paging.DefaultPaginator
import com.outfitgo.store.presentation.util.paging.Paginator
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
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class BrandProductsViewModel @Inject constructor(
    private val getBrandProductsUseCase: GetBrandProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(BrandProductsState())
    val uiState = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    private var brandName: String = ""

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    _uiState.update {
                        it.copy(products = emptyList())
                    }
                    brandProductsPaginator.reset()
                    getNextBrandProducts()
                }
        }
    }

    private val brandProductsPaginator: Paginator<String?, CommonProduct> = DefaultPaginator(
        initialKey = null,
        isEndReached = { products ->
            products.isEmpty()
        },
        getNextKey = { products ->
            products.last().pageCursor
        },
        onRequest = { nextKey ->
            getBrandProductsUseCase.execute(
                brand = brandName,
                searchQuery = _searchQuery.value,
                first = 8,
                after = nextKey
            )
        },
        onLoadUpdated = { isLoading ->
            _uiState.update {
                it.copy(isLoading = isLoading)
            }
        },
        onSuccess = { newProducts ->
            _uiState.update {
                it.copy(
                    products = uiState.value.products + newProducts,
                    productsEndReached = newProducts.isEmpty()
                )
            }
        },
        onError = { throwable ->
            _uiState.update {
                it.copy(productsLoadingError = throwable?.message.toString())
            }
        }
    )

    fun setBrandName(brandName: String) {
        this.brandName = brandName
    }

    fun getNextBrandProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            brandProductsPaginator.loadNextItems()
        }
    }

    fun processIntent(intent: BrandProductsIntent) {
        when (intent) {
            is BrandProductsIntent.ChangeSearchQuery -> _searchQuery.update { intent.query }
            is BrandProductsIntent.GetNextProducts -> getNextBrandProducts()
            else -> Unit
        }
    }
}