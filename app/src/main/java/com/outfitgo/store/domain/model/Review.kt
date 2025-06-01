package com.outfitgo.store.domain.model

data class Review(
    val id: String,
    val reviewerName: String,
    val rating: Double,
    val comment: String,
    val dateString: String
)