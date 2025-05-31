package com.outfitgo.store.core.di.modules

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.AdminApollo
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.remote.brand.BrandsRemoteDataSource
import com.outfitgo.store.data.datasource.remote.brand.BrandsRemoteDataSourceImpl
import com.outfitgo.store.data.repository.brand.BrandsRepositoryImpl
import com.outfitgo.store.domain.repository.brand.BrandsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BrandsModule {
    @Provides
    @Singleton
    fun provideBrandsRemoteDataSource(@StorefrontApollo remoteClient: ApolloClient): BrandsRemoteDataSource {
        return BrandsRemoteDataSourceImpl(remoteClient)
    }

    @Provides
    @Singleton
    fun provideBrandsRepository(brandsRemoteDataSource: BrandsRemoteDataSource): BrandsRepository{
        return BrandsRepositoryImpl(brandsRemoteDataSource)
    }
}