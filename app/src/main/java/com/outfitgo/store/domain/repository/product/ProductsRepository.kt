package com.outfitgo.store.domain.repository.product

import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.domain.model.product.DetailedProduct

interface ProductsRepository {
    suspend fun fetchLatestProducts(
        first: Int,
        after: String?
    ):List<CommonProduct>

    suspend fun fetchProductById(id: String, variantCount: Int = 3): DetailedProduct
}