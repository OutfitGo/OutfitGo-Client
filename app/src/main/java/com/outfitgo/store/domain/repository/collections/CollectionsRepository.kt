package com.outfitgo.store.domain.repository.collections

import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.model.product.Product

interface CollectionsRepository {
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