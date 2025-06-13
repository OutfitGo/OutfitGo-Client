package com.outfitgo.store.data.mappers

import com.outfitgo.store.domain.model.order.Order
import com.outfitgo.store.domain.model.order.OrderContactInfo
import com.outfitgo.store.domain.model.order.OrderShippingAddress
import com.outfitgo.store.domain.model.order.OrdersResponse
import com.outfitgo.store.storefront.CustomerOrdersQuery

fun CustomerOrdersQuery.Customer.toOrderResponse(): OrdersResponse {
    return OrdersResponse(
        ordersCount = this.orders.totalCount.toString().toInt(),
        orders = this.orders.edges.map {
            it.toOrder(
                contactInfo = OrderContactInfo(
                    name = this.displayName.toString(),
                    email = this.email.toString()
                )
            )
        }
    )
}

fun CustomerOrdersQuery.ShippingAddress.toOrderShippingAddress(): OrderShippingAddress {
    return OrderShippingAddress(
        firstName = firstName.toString(),
        secondName = lastName.toString(),
        city = city.toString(),
        addressLine = address1.toString()
    )
}

fun CustomerOrdersQuery.Edge.toOrder(contactInfo: OrderContactInfo): Order {
    return Order(
        id = this.node.id,
        number = this.node.orderNumber,
        date = this.node.processedAt.toString(),
        paymentStatus = this.node.financialStatus?.name.toString(),
        totalPrice = this.node.currentTotalPrice.amount.toString(),
        itemsCount = this.node.lineItems.edges.size,
        items = this.node.lineItems.edges.map { it.toOrderProduct() },
        trackingUrl = this.node.statusUrl.toString(),
        shippingAddress = this.node.shippingAddress?.toOrderShippingAddress(),
        contactInfo = contactInfo,
        pageCursor = this.cursor
    )
}



