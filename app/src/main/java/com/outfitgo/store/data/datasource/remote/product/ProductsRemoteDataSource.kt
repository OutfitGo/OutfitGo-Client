package com.outfitgo.store.data.datasource.remote.product

import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.domain.model.product.DetailedProduct

interface ProductsRemoteDataSource {
    suspend fun fetchLatestProducts(
        first: Int,
        after: String?
    ): List<CommonProduct>

    suspend fun fetchProductById(id: String, variantCount: Int = 3): DetailedProduct
}

