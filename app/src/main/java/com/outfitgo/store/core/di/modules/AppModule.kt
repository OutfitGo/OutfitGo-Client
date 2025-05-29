package com.outfitgo.store.core.di.modules

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.BuildConfig
import com.outfitgo.store.core.di.qualifiers.AdminApollo
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.remote.brand.BrandsRemoteDataSource
import com.outfitgo.store.data.datasource.remote.brand.BrandsRemoteDataSourceImpl
import com.outfitgo.store.data.datasource.remote.product.ProductsRemoteDataSource
import com.outfitgo.store.data.datasource.remote.product.ProductsRemoteDataSourceImpl
import com.outfitgo.store.data.repository.brand.BrandsRepositoryImpl
import com.outfitgo.store.data.repository.product.ProductsRepositoryImpl
import com.outfitgo.store.domain.repository.brand.BrandsRepository
import com.outfitgo.store.domain.repository.product.ProductsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    const val SERVER_URL = "https://mad45-sv-and3.myshopify.com/api/2025-04/graphql.json"
    const val ADMIN_SERVER_URL = "https://mad45-sv-and3.myshopify.com/admin/api/2025-04/graphql.json"

    @Provides
    @Singleton
    @StorefrontApollo
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder().apply {
            serverUrl(SERVER_URL)
            addHttpHeader(
                name = "X-Shopify-Storefront-Access-Token",
                value = BuildConfig.SHOPIFY_STORE_FRONT_ACCESS_TOKEN
            )
        }.build()
    }

    @Provides
    @Singleton
    @AdminApollo
    fun provideAdminApolloClient(): ApolloClient {
        return ApolloClient.Builder().apply {
            serverUrl(ADMIN_SERVER_URL)
            addHttpHeader(
                name = "X-Shopify-Access-Token",
                value = BuildConfig.SHOPIFY_ADMIN_ACCESS_TOKEN
            )
        }.build()
    }

}