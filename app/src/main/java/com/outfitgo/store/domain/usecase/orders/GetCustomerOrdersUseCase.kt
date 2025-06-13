package com.outfitgo.store.domain.usecase.orders

import com.outfitgo.store.core.util.exceptions.MissingUserTokenException
import com.outfitgo.store.domain.model.order.OrdersResponse
import com.outfitgo.store.domain.repository.orders.OrdersRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class GetCustomerOrdersUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val usersRepository: UsersRepository
) {
    suspend fun execute(
        first: Int,
        after: String?
    ): OrdersResponse {
        val userToken = usersRepository.getSavedUserToken()

        if (userToken.isNullOrBlank()) {
            throw MissingUserTokenException()
        }

        return ordersRepository.getCustomerOrders(
            customerToken = userToken,
            first = first,
            after = after
        ) ?: throw Exception("Can't fetch your orders, try again later")
    }
}