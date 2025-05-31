package com.outfitgo.store.data.datasource.remote.brand

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.outfitgo.store.data.mappers.toBrand
import com.outfitgo.store.data.mappers.toCommonProduct
import com.outfitgo.store.domain.model.brand.Brand
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.storefront.BrandProductsQuery
import com.outfitgo.store.storefront.BrandsQuery
import javax.inject.Inject

class BrandsRemoteDataSourceImpl @Inject constructor(
    private val remoteClient: ApolloClient
) : BrandsRemoteDataSource {

    override suspend fun fetchAllBrands(
        first: Int,
        after: String?
    ): List<Brand> {
        val brandsResponse = remoteClient.query(
            BrandsQuery(
                first = first,
                after = if (after.isNullOrBlank()) Optional.absent() else Optional.present(after)
            )
        ).execute()

        if (brandsResponse.hasErrors()) {
            throw Exception(brandsResponse.errors?.first()?.message)
        }

        val brands = brandsResponse.dataAssertNoErrors.collections.edges.map {
            it.toBrand()
        }

        return brands
    }

    override suspend fun fetchBrandProducts(
        brand: String,
        searchQuery: String,
        first: Int,
        after: String?
    ): List<CommonProduct> {
        val brandProductsResponse = remoteClient.query(
            BrandProductsQuery(
                searchQuery = "vendor:'$brand' title:${if (searchQuery.isBlank()) "*" else "*$searchQuery*"}",
                first = first,
                after = if (after.isNullOrBlank()) Optional.absent() else Optional.Present(after)
            )
        ).execute()

        if (brandProductsResponse.hasErrors()) {
            throw Exception(brandProductsResponse.errors?.first()?.message)
        }

        val brandProducts = brandProductsResponse.dataAssertNoErrors.products.edges.map {
            it.toCommonProduct()
        }

        return brandProducts
    }
}