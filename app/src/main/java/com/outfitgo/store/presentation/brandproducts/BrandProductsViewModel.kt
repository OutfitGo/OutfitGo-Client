package com.outfitgo.store.presentation.brandproducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.Const.PAGE_SIZE
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.usecase.collections.GetBrandProductsUseCase
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

    private val _searchQuery = MutableStateFlow<String?>(null)

    private var brandName: String = ""

    init {
        viewModelScope.launch {
            _searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .collectLatest { query ->
                    if (query == null) return@collectLatest
                    _uiState.update {
                        it.copy(products = emptyList())
                    }
                    brandProductsPaginator.reset()
                    getNextBrandProducts()
                }
        }
    }

    private val brandProductsPaginator: Paginator<String?, Product> = DefaultPaginator(
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
                productName = _searchQuery.value ?: "",
                first = PAGE_SIZE,
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
                    productsEndReached = newProducts.size < PAGE_SIZE
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