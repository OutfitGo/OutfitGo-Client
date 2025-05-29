package com.outfitgo.store.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponseDTO(
    val meta: Meta,
    val data: Map<String, CurrencyDTO>
)

@Serializable
data class Meta(
    val last_updated_at: String
)

@Serializable
data class CurrencyDTO(
    val code: String,
    val value: Double
)
