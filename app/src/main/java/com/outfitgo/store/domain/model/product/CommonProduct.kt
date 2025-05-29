package com.outfitgo.store.domain.model.product

data class CommonProduct(
    val id: String,
    val name: String,
    val type: String,
    val price: String,
    val imageUrl: String,
    val vendor: String,
    val pageCursor: String
)