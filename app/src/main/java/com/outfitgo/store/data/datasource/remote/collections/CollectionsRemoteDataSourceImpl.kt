package com.outfitgo.store.data.datasource.remote.collections

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.outfitgo.store.data.mappers.toCollection
import com.outfitgo.store.data.mappers.toProduct
import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.storefront.BrandCollectionProductsQuery
import com.outfitgo.store.storefront.CategoryCollectionProductsQuery
import com.outfitgo.store.storefront.CollectionsQuery
import javax.inject.Inject

class CollectionsRemoteDataSourceImpl @Inject constructor(
    private val remoteClient: ApolloClient
) : CollectionsRemoteDataSource {

    override suspend fun fetchCollections(): List<Collection> {
        val collectionsResponse = remoteClient.query(CollectionsQuery()).execute()

        if (collectionsResponse.hasErrors()) {
            throw Exception(collectionsResponse.errors?.first()?.message)
        }

        val brands = collectionsResponse.dataAssertNoErrors.collections.edges.map {
            it.toCollection()
        }

        return brands
    }


    override suspend fun fetchBrandCollectionProducts(
        collection: String,
        productName: String?,
        first: Int,
        after: String?
    ): List<Product> {
        val brandProductsResponse = remoteClient.query(
            BrandCollectionProductsQuery(
                searchQuery = getBrandSearchQuery(
                    brandName = collection,
                    productName = productName
                ),
                first = first,
                after = if (after.isNullOrBlank()) Optional.absent() else Optional.Present(after)
            )
        ).execute()

        if (brandProductsResponse.hasErrors()) {
            throw Exception(brandProductsResponse.errors?.first()?.message)
        }

        val brandProducts = brandProductsResponse.dataAssertNoErrors.products.edges.map {
            it.toProduct()
        }

        return brandProducts
    }

    override suspend fun fetchCategoryCollectionProducts(categoryHandle: String): List<Product> {
        val categoryProductsResponse = remoteClient.query(
            CategoryCollectionProductsQuery(
                handle = categoryHandle
            )
        ).execute()

        if(categoryProductsResponse.hasErrors()){
            throw Exception(categoryProductsResponse.errors?.first()?.message)
        }

        val categoryProducts = categoryProductsResponse.dataAssertNoErrors.collection?.products?.edges?.map {
            it.toProduct()
        } ?: emptyList()

        return categoryProducts
    }

    private fun getBrandSearchQuery(
        brandName:String,
        productName:String?
    ): String{
        return "vendor:'$brandName' title:${if (productName.isNullOrBlank()) "*" else "*$productName*"}"
    }
}