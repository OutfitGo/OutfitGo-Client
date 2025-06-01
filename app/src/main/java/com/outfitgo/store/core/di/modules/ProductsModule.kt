package com.outfitgo.store.core.di.modules

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.AdminApollo
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.remote.product.ProductsRemoteDataSource
import com.outfitgo.store.data.datasource.remote.product.ProductsRemoteDataSourceImpl
import com.outfitgo.store.data.repository.product.ProductsRepositoryImpl
import com.outfitgo.store.domain.repository.product.ProductsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {
    @Provides
    @Singleton
    fun provideProductsRemoteDataSource(@StorefrontApollo remoteClient: ApolloClient): ProductsRemoteDataSource {
        return ProductsRemoteDataSourceImpl(remoteClient)
    }

    @Provides
    @Singleton
    fun provideProductsRepository(productsRemoteDataSource: ProductsRemoteDataSource): ProductsRepository{
        return ProductsRepositoryImpl(productsRemoteDataSource)
    }
}