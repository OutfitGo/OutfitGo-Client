package com.outfitgo.store.domain.repository.brand

import com.outfitgo.store.domain.model.brand.Brand


interface BrandsRepository {
    suspend fun fetchAllBrands(
        first: Int,
        after: String?
    ):List<Brand>
}