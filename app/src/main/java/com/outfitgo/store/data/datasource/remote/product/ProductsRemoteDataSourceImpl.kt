package com.outfitgo.store.data.datasource.remote.product

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.outfitgo.store.data.mappers.toProduct
import com.outfitgo.store.data.mappers.toDetailedProduct
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.model.product.DetailedProduct
import com.outfitgo.store.storefront.GetProductByIdQuery
import com.outfitgo.store.storefront.LatestProductsQuery
import javax.inject.Inject

class ProductsRemoteDataSourceImpl @Inject constructor(
    private val remoteClient: ApolloClient
): ProductsRemoteDataSource {
    override suspend fun fetchLatestProducts(
        first: Int,
        after: String?
    ): List<Product> {
        val latestProductsResponse = remoteClient.query(
            LatestProductsQuery(
                first = first,
                after = if(after.isNullOrBlank()) Optional.absent() else Optional.present(after)
            )
        ).execute()

        if (latestProductsResponse.hasErrors()){
            throw Exception(latestProductsResponse.errors?.first()?.message)
        }

        val latestProducts = latestProductsResponse.dataAssertNoErrors.products.edges.map {
            it.toProduct()
        }

        return latestProducts
    }

    override suspend fun fetchProductById(
        id: String,
        variantCount: Int
    ): DetailedProduct {
        val query = GetProductByIdQuery(id, variantCount)
        val response = remoteClient.query(query).execute()
        val data = response.data
        if(data?.product == null) {
            throw Exception("Failed to Get Product")
        }

        val product = data.product
        return product.toDetailedProduct()
    }
}



