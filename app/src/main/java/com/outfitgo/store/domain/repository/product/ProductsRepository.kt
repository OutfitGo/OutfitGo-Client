package com.outfitgo.store.domain.repository.product

import com.outfitgo.store.domain.model.product.CommonProduct

interface ProductsRepository {
    suspend fun fetchLatestProducts(
        first: Int,
        after: String?
    ):List<CommonProduct>
}