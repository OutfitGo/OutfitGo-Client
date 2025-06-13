package com.outfitgo.store.domain.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderShippingAddress(
    val firstName:String,
    val secondName: String,
    val city:String,
    val addressLine:String
)
