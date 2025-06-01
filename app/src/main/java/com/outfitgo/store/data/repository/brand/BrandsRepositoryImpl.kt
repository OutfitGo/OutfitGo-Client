package com.outfitgo.store.data.repository.brand

import com.outfitgo.store.data.datasource.remote.brand.BrandsRemoteDataSource
import com.outfitgo.store.domain.model.brand.Brand
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.domain.repository.brand.BrandsRepository
import javax.inject.Inject

class BrandsRepositoryImpl @Inject constructor(
    private val brandsRemoteDataSource: BrandsRemoteDataSource
): BrandsRepository {

    override suspend fun fetchAllBrands(
        first: Int,
        after: String?
    ): List<Brand> {
        return brandsRemoteDataSource.fetchAllBrands(
            first = first,
            after = after
        )
    }

    override suspend fun fetchBrandProducts(
        brand: String,
        searchQuery: String,
        first: Int,
        after: String?
    ): List<CommonProduct> {
        return brandsRemoteDataSource.fetchBrandProducts(
            brand = brand,
            searchQuery = searchQuery,
            first = first,
            after = after
        )
    }
}