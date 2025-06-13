package com.outfitgo.store.domain.repository.orders

import com.outfitgo.store.domain.model.order.OrdersResponse

interface OrdersRepository {
    suspend fun getCustomerOrders(
        customerToken: String,
        first: Int,
        after: String?
    ): OrdersResponse?
}