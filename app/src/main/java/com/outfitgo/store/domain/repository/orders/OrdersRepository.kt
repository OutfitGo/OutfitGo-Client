package com.outfitgo.store.domain.repository.orders

import com.outfitgo.store.domain.model.FinancialStatus
import com.outfitgo.store.domain.model.cart.CartItem
import com.outfitgo.store.domain.model.order.OrderShippingAddress
import com.outfitgo.store.domain.model.order.OrdersResponse

interface OrdersRepository {
    suspend fun getCustomerOrders(
        customerToken: String,
        first: Int,
        after: String?
    ): OrdersResponse?

    suspend fun createOrder(
        customerEmail: String,
        financialStatus: FinancialStatus,
        shippingAddress: OrderShippingAddress,
        cartItems: List<CartItem>,
    ): Boolean
}