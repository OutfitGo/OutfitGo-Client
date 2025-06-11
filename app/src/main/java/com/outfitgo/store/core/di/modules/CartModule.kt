package com.outfitgo.store.core.di.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.local.cart.CartLocalDataSource
import com.outfitgo.store.data.datasource.local.cart.CartLocalDataSourceImpl
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
    fun provideCartLocalDataSource(dataStore: DataStore<Preferences>): CartLocalDataSource {
        return CartLocalDataSourceImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        remote: CartRemoteDataSource,
        local: CartLocalDataSource
    ): CartRepository {
        return CartRepositoryImpl(remote, local)
    }
}