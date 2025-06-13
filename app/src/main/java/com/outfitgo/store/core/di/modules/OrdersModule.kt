package com.outfitgo.store.core.di.modules

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.remote.order.OrdersRemoteDataSource
import com.outfitgo.store.data.datasource.remote.order.OrdersRemoteDataSourceImpl
import com.outfitgo.store.data.repository.orders.OrdersRepositoryImpl
import com.outfitgo.store.domain.repository.orders.OrdersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrdersModule {
    @Provides
    @Singleton
    fun provideOrdersRemoteDataSource(@StorefrontApollo remoteClient: ApolloClient): OrdersRemoteDataSource {
        return OrdersRemoteDataSourceImpl(remoteClient = remoteClient)
    }

    @Provides
    @Singleton
    fun provideOrdersRepository(remoteDataSource: OrdersRemoteDataSource): OrdersRepository {
        return OrdersRepositoryImpl(remoteDataSource = remoteDataSource)
    }
}