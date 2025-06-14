package com.outfitgo.store.domain.model.product

import com.outfitgo.store.domain.model.Review


data class DetailedProduct(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val imageUrls: List<String>,
    val tags: List<String>,
    val vendor: String,
    val category: String,
    val rating: Int,
    val reviews: List<Review>,
    val currencyCode: String,
    val variants: List<ProductVariant>
)

