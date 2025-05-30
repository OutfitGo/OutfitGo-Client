package com.outfitgo.store.data.datasource.remote.product

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Optional
import com.outfitgo.store.admin.LatestProductsQuery
import com.outfitgo.store.data.mappers.toCommonProduct
import com.outfitgo.store.domain.model.product.CommonProduct
import javax.inject.Inject

class ProductsRemoteDataSourceImpl @Inject constructor(
    private val remoteClient: ApolloClient
): ProductsRemoteDataSource {
    override suspend fun fetchLatestProducts(
        first: Int,
        after: String?
    ): List<CommonProduct> {
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
            it.toCommonProduct()
        }

        return latestProducts
    }
}