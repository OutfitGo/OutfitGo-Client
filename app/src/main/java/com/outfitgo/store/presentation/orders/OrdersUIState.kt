package com.outfitgo.store.presentation.orders

import com.outfitgo.store.domain.model.order.Order

data class OrdersUIState(
    val orders: List<Order> = emptyList(),
    val totalOrdersCount: Int? = null,
    val isNextPageLoading: Boolean = false,
    val isEndReached: Boolean = false,
    val isUserLoggedIn: Boolean = true,
    val errorMsg: String? = null
)
