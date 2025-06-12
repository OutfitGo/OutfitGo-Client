package com.outfitgo.store.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.Const
import com.outfitgo.store.domain.usecase.cart.AddBuyerToCartUseCase
import com.outfitgo.store.domain.usecase.cart.CreateCartUseCase
import com.outfitgo.store.domain.usecase.cart.GetCartIdUseCase
import com.outfitgo.store.domain.usecase.cart.SaveCartIdUseCase
import com.outfitgo.store.core.util.Const.PAGE_SIZE
import com.outfitgo.store.core.util.CurrencyExchange
import com.outfitgo.store.core.util.CurrencyUnit
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.usecase.collections.GetBrandsUseCase
import com.outfitgo.store.domain.usecase.coupon.GetCouponsUseCase
import com.outfitgo.store.domain.usecase.products.GetLatestProductsUseCase
import com.outfitgo.store.domain.usecase.settings.GetCurrencyUnitUseCase
import com.outfitgo.store.domain.usecase.settings.GetLatestExchangeRateUseCase
import com.outfitgo.store.presentation.util.paging.DefaultPaginator
import com.outfitgo.store.presentation.util.paging.Paginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBrandsUseCase: GetBrandsUseCase,
    private val getLatestProductsUseCase: GetLatestProductsUseCase,
    private val getCurrencyUnitUseCase: GetCurrencyUnitUseCase,
    private val getLatestExchangeRateUseCase: GetLatestExchangeRateUseCase,
    private val getCouponsUseCase: GetCouponsUseCase,
    private val createCartUseCase: CreateCartUseCase,
    private val addBuyerToCartUseCase: AddBuyerToCartUseCase,
    private val getCartIdUseCase: GetCartIdUseCase,
    private val saveCartIdUseCase: SaveCartIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeState>(HomeState())
    val uiState = _uiState.asStateFlow()

    private fun cartInit(){
        viewModelScope.launch(Dispatchers.IO){
            getCartIdUseCase.execute().collect{
                if (it.isBlank()){
                    val cartId = createCartUseCase.execute()
                    Const.cartId =cartId
                    saveCartIdUseCase.execute(cartId)
                    /*if (Const.isLoggedIn){
                      //TODO addCart to buyer
                    }*/
                }else{
                    Const.cartId=it
                }
            }
        }
    }

    

    private fun observeCurrencyAndRate() {
        viewModelScope.launch {
            getCurrencyUnitUseCase.execute()
                .distinctUntilChanged()
                .collectLatest { currency ->
                    CurrencyExchange.currentCurrencyUnit = currency.name
                    if (currency != CurrencyUnit.EGP) {
                        val rate =
                            getLatestExchangeRateUseCase.execute(targetCurrency = currency).value
                        CurrencyExchange.rate = rate
                    }
                }
        }
    }

    private val latestProductsPaginator: Paginator<String?, Product> = DefaultPaginator(
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
            _uiState.update {
                it.copy(isLatestProductsLoading = isLoading)
            }
        },
        onSuccess = { newProducts ->
            _uiState.update {
                it.copy(
                    latestProducts = uiState.value.latestProducts + newProducts,
                    latestProductsEndReached = newProducts.size < PAGE_SIZE
                )
            }
        },
        onError = { throwable ->
            _uiState.update {
                it.copy(latestProductsLoadingError = throwable?.message.toString())
            }
        }
    )
    init {
        getBrands()
        observeCurrencyAndRate()
        getNextLatestProducts()
        getCoupons()
        cartInit()
    }

    fun getBrands() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val brands = getBrandsUseCase.execute()
                _uiState.update {
                    it.copy(
                        isBrandsLoading = false,
                        brands = brands
                    )
                }
            } catch (exception: Exception) {
                //TODO Handle Error
            }
        }
    }

    fun getNextLatestProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            latestProductsPaginator.loadNextItems()
        }
    }

    private fun getCoupons() {
        viewModelScope.launch(Dispatchers.IO) {
            val coupons = getCouponsUseCase.execute()
            _uiState.update { it.copy(coupons = coupons) }
        }
    }

    fun processIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.GetNextBrands -> getBrands()
            is HomeIntent.GetNextLatestProducts -> getNextLatestProducts()
            is HomeIntent.GetCoupons -> getCoupons()
            else -> Unit
        }
    }
}