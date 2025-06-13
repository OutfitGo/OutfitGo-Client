package com.outfitgo.store.domain.usecase.orders

import com.outfitgo.store.domain.model.FinancialStatus
import com.outfitgo.store.domain.model.cart.CartItem
import com.outfitgo.store.domain.model.order.OrderShippingAddress
import com.outfitgo.store.domain.repository.orders.OrdersRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository,
) {
    suspend fun execute(
        financialStatus: FinancialStatus,
        shippingAddress: OrderShippingAddress,
        cartItems: List<CartItem>,
    ): Boolean {
        return ordersRepository.createOrder(
            customerEmail = "mahmoudewida3@gmail.com", //TODO change this to the real email
            financialStatus = financialStatus,
            shippingAddress = shippingAddress,
            cartItems = cartItems
        )
    }
}