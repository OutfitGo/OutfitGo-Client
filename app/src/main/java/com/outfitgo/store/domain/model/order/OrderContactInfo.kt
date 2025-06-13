package com.outfitgo.store.domain.model.order

import kotlinx.serialization.Serializable

@Serializable
data class OrderContactInfo(
    val name: String,
    val email: String,
)
