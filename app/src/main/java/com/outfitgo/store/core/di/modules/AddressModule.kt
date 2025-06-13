package com.outfitgo.store.core.di.modules

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.remote.address.AddressRemoteDataSourceImpl
import com.outfitgo.store.data.repository.address.AddressRemoteDataSource
import com.outfitgo.store.data.repository.address.AddressRepositoryImpl
import com.outfitgo.store.domain.repository.address.AddressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AddressModule {
    @Provides
    fun provideAddressRemoteDataSource(@StorefrontApollo client:ApolloClient):AddressRemoteDataSource{
        return AddressRemoteDataSourceImpl(client)
    }

    @Provides
    fun provideAddressRepository(addressRemoteDataSource: AddressRemoteDataSource): AddressRepository {
        return AddressRepositoryImpl(addressRemoteDataSource)
    }

    @Provides
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        return Places.createClient(context)
    }

}