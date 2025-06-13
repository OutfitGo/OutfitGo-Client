package com.outfitgo.store.presentation.orders

import com.outfitgo.store.domain.model.order.Order

sealed interface OrdersIntent {
    object GoBack : OrdersIntent
    object GetNextOrders: OrdersIntent
    object GoToLogin : OrdersIntent
    data class OpenOrderDetails(val order: Order) : OrdersIntent
}