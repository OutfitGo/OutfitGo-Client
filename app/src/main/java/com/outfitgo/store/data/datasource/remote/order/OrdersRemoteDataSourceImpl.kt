package com.outfitgo.store.data.datasource.remote.order

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.outfitgo.store.data.mappers.toOrderResponse
import com.outfitgo.store.domain.model.order.OrdersResponse
import com.outfitgo.store.storefront.CustomerOrdersQuery
import javax.inject.Inject

class OrdersRemoteDataSourceImpl @Inject constructor(
    private val remoteClient: ApolloClient
) : OrdersRemoteDataSource {
    override suspend fun getCustomerOrders(
        customerToken: String,
        first: Int,
        after: String?
    ):OrdersResponse? {
        val response = remoteClient.query(
            CustomerOrdersQuery(
                customerAccessToken = customerToken,
                first = first,
                after = if(after.isNullOrBlank()) Optional.absent() else Optional.present(after)
            )
        ).execute()

        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }

        val ordersResponse = response.dataAssertNoErrors.customer?.toOrderResponse()

        return ordersResponse
    }
}