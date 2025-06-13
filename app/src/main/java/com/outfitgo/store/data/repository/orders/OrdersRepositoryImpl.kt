package com.outfitgo.store.data.repository.orders

import com.outfitgo.store.data.datasource.remote.order.OrdersRemoteDataSource
import com.outfitgo.store.domain.model.order.OrdersResponse
import com.outfitgo.store.domain.repository.orders.OrdersRepository
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val remoteDataSource: OrdersRemoteDataSource
): OrdersRepository {
    override suspend fun getCustomerOrders(
        customerToken: String,
        first: Int,
        after: String?
    ): OrdersResponse? {
        return remoteDataSource.getCustomerOrders(
            customerToken = customerToken,
            first = first,
            after = after
        )
    }
}