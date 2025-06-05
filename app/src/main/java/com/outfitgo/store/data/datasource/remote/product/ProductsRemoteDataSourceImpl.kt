package com.outfitgo.store.data.datasource.remote.product

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.outfitgo.store.data.mappers.toCommonProduct
import com.outfitgo.store.data.mappers.toDetailedProduct
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.domain.model.product.DetailedProduct
import com.outfitgo.store.storefront.GetProductByIdQuery
import com.outfitgo.store.storefront.GetProductsByTitleQuery
import com.outfitgo.store.storefront.LatestProductsQuery
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

    override suspend fun fetchProductsByTitle(title: String): List<CommonProduct> {
        val searchQuery = "title:*${title}*" // to get anything like the title . if empty string provided empty list will come
        val query = GetProductsByTitleQuery(searchQuery = searchQuery)
        val response = remoteClient.query(query).execute()
        val data = response.data
        if(data?.products == null) {
            throw Exception("Products are NULL")
        }
        val products = data.products.nodes.map {
            it.toCommonProduct()
        }
        return products
    }
}

fun GetProductsByTitleQuery.Node.toCommonProduct(): CommonProduct {
    return CommonProduct(
        id = this.id,
        name = this.title,
        type = this.productType,
        price = "${this.priceRange.minVariantPrice.amount}",
        imageUrl = "${this.images.nodes.first().url}",
        vendor = this.vendor,
        pageCursor = "" // i won't use pagination in search
    )
}


