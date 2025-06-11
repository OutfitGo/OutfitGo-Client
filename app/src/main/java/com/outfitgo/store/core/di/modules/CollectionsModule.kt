package com.outfitgo.store.core.di.modules

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.remote.collections.CollectionsRemoteDataSource
import com.outfitgo.store.data.datasource.remote.collections.CollectionsRemoteDataSourceImpl
import com.outfitgo.store.data.repository.collections.CollectionsRepositoryImpl
import com.outfitgo.store.domain.repository.collections.CollectionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CollectionsModule {
    @Provides
    @Singleton
    fun provideCollectionsRemoteDataSource(@StorefrontApollo remoteClient: ApolloClient): CollectionsRemoteDataSource {
        return CollectionsRemoteDataSourceImpl(remoteClient)
    }

    @Provides
    @Singleton
    fun provideCollectionsRepository(brandsRemoteDataSource: CollectionsRemoteDataSource): CollectionsRepository{
        return CollectionsRepositoryImpl(brandsRemoteDataSource)
    }
}