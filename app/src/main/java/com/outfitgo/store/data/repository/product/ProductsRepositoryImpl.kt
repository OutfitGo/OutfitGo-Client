package com.outfitgo.store.data.repository.product

import com.outfitgo.store.data.datasource.remote.product.ProductsRemoteDataSource
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.domain.model.product.DetailedProduct
import com.outfitgo.store.domain.repository.product.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsRemoteDataSource: ProductsRemoteDataSource
) : ProductsRepository {
    override suspend fun fetchLatestProducts(
        first: Int,
        after: String?
    ): List<CommonProduct> {
        return productsRemoteDataSource.fetchLatestProducts(
            first = first,
            after = after
        )
    }

    override suspend fun fetchProductById(
        id: String,
        variantCount: Int
    ): DetailedProduct {
        return productsRemoteDataSource.fetchProductById(id, variantCount)
    }
}