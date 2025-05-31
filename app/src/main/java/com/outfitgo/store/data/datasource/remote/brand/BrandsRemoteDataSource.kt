package com.outfitgo.store.data.datasource.remote.brand

import com.outfitgo.store.domain.model.brand.Brand
import com.outfitgo.store.domain.model.product.CommonProduct

interface BrandsRemoteDataSource {
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