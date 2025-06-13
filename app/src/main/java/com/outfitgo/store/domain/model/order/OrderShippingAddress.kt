package com.outfitgo.store.domain.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderShippingAddress(
    val name:String,
    val city:String,
    val addressLine1:String,
    val addressLine2:String,
)
