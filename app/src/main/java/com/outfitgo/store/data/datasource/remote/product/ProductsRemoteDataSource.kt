package com.outfitgo.store.data.datasource.remote.product

import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.model.product.DetailedProduct

interface ProductsRemoteDataSource {
    suspend fun fetchLatestProducts(
        first: Int,
        after: String?
    ): List<Product>

    suspend fun fetchProductById(id: String, variantCount: Int = 3): DetailedProduct
}

