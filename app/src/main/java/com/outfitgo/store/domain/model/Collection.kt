package com.outfitgo.store.domain.model

data class Collection(
    val id: String,
    val name: String,
    val handle: String,
    val imageUrl: String,
    val pageCursor: String
)