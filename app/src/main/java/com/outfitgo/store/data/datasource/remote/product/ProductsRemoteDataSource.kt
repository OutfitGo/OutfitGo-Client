package com.outfitgo.store.data.datasource.remote.product

import com.outfitgo.store.domain.model.product.CommonProduct

interface ProductsRemoteDataSource {
    suspend fun fetchLatestProducts(
        first: Int,
        after: String?
    ): List<CommonProduct>
}