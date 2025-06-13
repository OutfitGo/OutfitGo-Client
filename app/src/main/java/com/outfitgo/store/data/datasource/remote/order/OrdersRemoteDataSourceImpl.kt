package com.outfitgo.store.data.datasource.remote.order

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.outfitgo.store.admin.OrderCreationMutation
import com.outfitgo.store.admin.type.MailingAddressInput
import com.outfitgo.store.admin.type.OrderCreateFinancialStatus
import com.outfitgo.store.admin.type.OrderCreateLineItemInput
import com.outfitgo.store.core.di.qualifiers.AdminApollo
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.mappers.toOrderResponse
import com.outfitgo.store.domain.model.FinancialStatus
import com.outfitgo.store.domain.model.cart.CartItem
import com.outfitgo.store.domain.model.order.OrderShippingAddress
import com.outfitgo.store.domain.model.order.OrdersResponse
import com.outfitgo.store.storefront.CustomerOrdersQuery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrdersRemoteDataSourceImpl @Inject constructor(
    private val adminClient: ApolloClient,
    private val storefrontClient: ApolloClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : OrdersRemoteDataSource {
    override suspend fun getCustomerOrders(
        customerToken: String,
        first: Int,
        after: String?,
    ): OrdersResponse? {
        return withContext(dispatcher) {
            val query = CustomerOrdersQuery(
                customerAccessToken = customerToken,
                first = first,
                after = if (after.isNullOrBlank()) Optional.absent() else Optional.present(after)
            )

            val response = storefrontClient.query(query).execute()

            if (response.hasErrors()) {
                throw Exception(response.errors?.first()?.message)
            }

            response.dataAssertNoErrors.customer?.toOrderResponse()
        }
    }

    override suspend fun createOrder(
        customerEmail: String,
        financialStatus: FinancialStatus,
        shippingAddress: OrderShippingAddress,
        cartItems: List<CartItem>,
    ): Boolean {
        return withContext(dispatcher) {
            val financialStatus = when (financialStatus) {
                FinancialStatus.PAID -> OrderCreateFinancialStatus.PAID
                FinancialStatus.PENDING -> OrderCreateFinancialStatus.PENDING
            }

            val shippingAddress = MailingAddressInput(
                firstName = Optional.present(shippingAddress.firstName),
                lastName = Optional.present(shippingAddress.secondName),
                city = Optional.present(shippingAddress.city),
                country = Optional.present("EG"),
                address1 = Optional.present(shippingAddress.addressLine),
                address2 = Optional.present(""),
                company = Optional.present(""),
                province = Optional.present(""),
                zip = Optional.present(""),
                provinceCode = Optional.present("")
            )

            val mutationListItems = cartItems.map {
                OrderCreateLineItemInput(
                    variantId = Optional.present(it.merchandise.variantId),
                    quantity = it.quantity
                )
            }

            val response = adminClient.mutation(
                OrderCreationMutation(
                    email = customerEmail,
                    financialStatus = financialStatus,
                    lineItems = mutationListItems,
                    shippingAddress = shippingAddress
                )
            ).execute()

            if (response.hasErrors()) {
                throw Exception(response.errors?.first()?.message)
            }

            response.dataAssertNoErrors.orderCreate?.userErrors?.isEmpty() == true
        }
    }
}
