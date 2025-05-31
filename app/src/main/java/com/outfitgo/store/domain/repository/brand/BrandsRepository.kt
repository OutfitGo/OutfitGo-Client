package com.outfitgo.store.domain.repository.brand

import com.outfitgo.store.domain.model.brand.Brand
import com.outfitgo.store.domain.model.product.CommonProduct

interface BrandsRepository {
    suspend fun fetchAllBrands(
        first: Int,
        after: String?
    ): List<Brand>

    suspend fun fetchBrandProducts(
        brand: String,
        searchQuery: String,
        first: Int,
        after: String?
    ): List<CommonProduct>
}