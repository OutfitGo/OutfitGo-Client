package com.outfitgo.store.domain.repository.product

import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.model.product.DetailedProduct

interface ProductsRepository {
    suspend fun fetchLatestProducts(
        first: Int,
        after: String?
    ):List<Product>

    suspend fun fetchProductById(id: String, variantCount: Int = 3): DetailedProduct

    suspend fun searchProductsByTitle(title: String): List<CommonProduct>
}