package com.outfitgo.store.core.di

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.BuildConfig
import com.outfitgo.store.core.di.qualifiers.AdminApollo
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
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