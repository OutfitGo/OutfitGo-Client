package com.outfitgo.store.core.di.modules

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.remote.cart.CartRemoteDataSource
import com.outfitgo.store.data.datasource.remote.cart.CartRemoteDataSourceImpl
import com.outfitgo.store.data.repository.cart.CartRepositoryImpl
import com.outfitgo.store.domain.repository.cart.CartRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CartModule {
    @Provides
    @Singleton
    fun provideCartRemoteDataSource(@StorefrontApollo remoteClient: ApolloClient): CartRemoteDataSource {
        return CartRemoteDataSourceImpl(remoteClient)
    }

    @Provides
    @Singleton
    fun provideCartRepository(remote: CartRemoteDataSource): CartRepository {
        return CartRepositoryImpl(remote)
    }
}