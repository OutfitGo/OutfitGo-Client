package com.outfitgo.store.data.datasource.remote.orders

import com.outfitgo.store.data.datasource.remote.order.OrdersRemoteDataSource
import com.outfitgo.store.domain.model.FinancialStatus
import com.outfitgo.store.domain.model.cart.CartItem
import com.outfitgo.store.domain.model.order.OrderShippingAddress
import com.outfitgo.store.domain.model.order.OrdersResponse

class FakeOrdersRemoteDataSource(
    private val ordersResponse: OrdersResponse? = null,
    private val createOrderSuccess: Boolean = true
) : OrdersRemoteDataSource {

    override suspend fun getCustomerOrders(
        customerToken: String,
        first: Int,
        after: String?
    ): OrdersResponse? {
        return ordersResponse
    }

    override suspend fun createOrder(
        customerEmail: String,
        financialStatus: FinancialStatus,
        shippingAddress: OrderShippingAddress,
        cartItems: List<CartItem>
    ): Boolean {
        return createOrderSuccess
    }
}