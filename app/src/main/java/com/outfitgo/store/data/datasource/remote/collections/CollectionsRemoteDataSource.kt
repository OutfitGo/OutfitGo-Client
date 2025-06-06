package com.outfitgo.store.data.datasource.remote.collections

import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.model.product.Product

interface CollectionsRemoteDataSource {
    suspend fun fetchCollections(): List<Collection>

    suspend fun fetchBrandCollectionProducts(
        brand: String,
        productName: String?,
        first: Int,
        after: String?
    ): List<Product>

    suspend fun fetchCategoryCollectionProducts(
        categoryHandle: String
    ): List<Product>
}