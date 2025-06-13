package com.outfitgo.store.domain.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderProduct(
    val id: String,
    val name: String,
    val price: String,
    val imageUrl: String,
    val quantity: Int,
    val variantTitle: String
)