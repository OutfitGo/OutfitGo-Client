package com.outfitgo.store.data.repository.collections

import com.outfitgo.store.data.datasource.remote.collections.CollectionsRemoteDataSource
import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.collections.CollectionsRepository
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(
    private val collectionsRemoteDataSource: CollectionsRemoteDataSource
): CollectionsRepository {

    override suspend fun fetchCollections(): List<Collection> {
        return collectionsRemoteDataSource.fetchCollections()
    }

    override suspend fun fetchBrandCollectionProducts(
        brand: String,
        productName: String?,
        first: Int,
        after: String?
    ): List<Product> {
        return collectionsRemoteDataSource.fetchBrandCollectionProducts(
            brand = brand,
            productName = productName,
            first = first,
            after = after
        )
    }

    override suspend fun fetchCategoryCollectionProducts(categoryHandle: String): List<Product> {
        return collectionsRemoteDataSource.fetchCategoryCollectionProducts(
            categoryHandle = categoryHandle
        )
    }
}