package com.outfitgo.store.domain.usecase.orders

import com.outfitgo.store.core.util.exceptions.MissingUserTokenException
import com.outfitgo.store.domain.model.FinancialStatus
import com.outfitgo.store.domain.model.cart.CartItem
import com.outfitgo.store.domain.model.order.OrderShippingAddress
import com.outfitgo.store.domain.repository.orders.OrdersRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val usersRepository: UsersRepository
) {
    suspend fun execute(
        financialStatus: FinancialStatus,
        shippingAddress: OrderShippingAddress,
        cartItems: List<CartItem>,
    ): Boolean {
        val email = usersRepository.getSavedUserEmail() ?:
        throw MissingUserTokenException()

        return ordersRepository.createOrder(
            customerEmail = email,
            financialStatus = financialStatus,
            shippingAddress = shippingAddress,
            cartItems = cartItems
        )
    }
}