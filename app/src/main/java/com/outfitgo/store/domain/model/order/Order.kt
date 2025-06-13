package com.outfitgo.store.domain.model.order

import com.outfitgo.store.domain.model.order.OrderProduct
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String,
    val number: Int,
    val date: String,
    val paymentStatus: String,
    val totalPrice: String,
    val items: List<OrderProduct>,
    val contactInfo: OrderContactInfo,
    val trackingUrl: String,
    val shippingAddress: OrderShippingAddress?,
    val itemsCount: Int,
    val pageCursor: String,
)
