package com.outfitgo.store.domain.model.order

data class OrdersResponse(
    val ordersCount: Int,
    val orders: List<Order>
)
