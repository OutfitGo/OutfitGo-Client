package com.outfitgo.store.presentation.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.Const.PAGE_SIZE
import com.outfitgo.store.core.util.exceptions.MissingUserTokenException
import com.outfitgo.store.domain.model.order.Order
import com.outfitgo.store.domain.usecase.orders.GetCustomerOrdersUseCase
import com.outfitgo.store.presentation.util.paging.DefaultPaginator
import com.outfitgo.store.presentation.util.paging.Paginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val getCustomerOrdersUseCase: GetCustomerOrdersUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OrdersUIState())
    val uiState = _uiState.asStateFlow()

    private val ordersPaginator: Paginator<String?, Order> = DefaultPaginator(
        initialKey = null,
        isEndReached = { orders ->
            orders.isEmpty()
        },
        getNextKey = { orders ->
            orders.last().pageCursor
        },
        onRequest = { nextKey ->
            val response = getCustomerOrdersUseCase.execute(first = 10, after = nextKey)
            if(uiState.value.totalOrdersCount == null){
                _uiState.update { it.copy(totalOrdersCount = response.ordersCount) }
            }
            response.orders
        },
        onLoadUpdated = { isLoading ->
            _uiState.update {
                it.copy(isNextPageLoading = isLoading)
            }
        },
        onSuccess = { newOrders ->
            _uiState.update {
                it.copy(
                    orders = uiState.value.orders + newOrders,
                    isEndReached = newOrders.size < PAGE_SIZE
                )
            }
        },
        onError = { throwable ->
            when (throwable) {
                is MissingUserTokenException -> {
                    _uiState.update {
                        it.copy(isUserLoggedIn = false)
                    }
                }
                else -> {
                    _uiState.update {
                        it.copy(errorMsg = throwable?.message.toString())
                    }
                }
            }
        }
    )

    init {
        getNextOrders()
    }

    fun getNextOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            ordersPaginator.loadNextItems()
        }
    }

    fun processIntent(intent: OrdersIntent) {
        when (intent) {
            is OrdersIntent.GetNextOrders -> getNextOrders()
            else -> Unit
        }
    }
}