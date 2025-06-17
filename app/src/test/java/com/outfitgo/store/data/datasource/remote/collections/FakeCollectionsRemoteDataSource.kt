package com.outfitgo.store.data.datasource.remote.collections

import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.model.Collection

class FakeCollectionsRemoteDataSource(
    private val collections: List<Collection> = emptyList(),
    private val products: List<Product> = emptyList()
) : CollectionsRemoteDataSource {

    override suspend fun fetchCollections(): List<Collection> = collections

    override suspend fun fetchBrandCollectionProducts(
        brand: String,
        productName: String?,
        first: Int,
        after: String?
    ): List<Product> {
        return products.filter {
            it.vendor == brand && (productName == null || it.name.contains(productName, ignoreCase = true))
        }.take(first)
    }

    override suspend fun fetchCategoryCollectionProducts(categoryHandle: String): List<Product> {
        return products.filter { it.type == categoryHandle }
    }
}