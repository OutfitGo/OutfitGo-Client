package com.outfitgo.store.data.datasource.remote.brand

import com.outfitgo.store.domain.model.brand.Brand

interface BrandsRemoteDataSource {
    suspend fun fetchAllBrands(
        first: Int,
        after: String?
    ): List<Brand>
}