package com.outfitgo.store.core.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.local.user.UserLocalDataSource
import com.outfitgo.store.data.datasource.local.user.UserLocalDataSourceImpl
import com.outfitgo.store.data.datasource.remote.user.UserRemoteDataSource
import com.outfitgo.store.data.datasource.remote.user.UserRemoteDataSourceImpl
import com.outfitgo.store.data.repository.user.UsersRepositoryImpl
import com.outfitgo.store.domain.repository.user.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUsersRepository(
        remoteDataSource: UserRemoteDataSource,
        localDataSource: UserLocalDataSource
    ): UsersRepository {
        return UsersRepositoryImpl(
            remoteDataSource,
            localDataSource = localDataSource
        )
    }

    @Provides
    @Singleton
    fun provideUserLocalDataSource(dataStore: DataStore<Preferences>): UserLocalDataSource {
        return UserLocalDataSourceImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideUsersRemoteDataSource(@StorefrontApollo client: ApolloClient): UserRemoteDataSource {
        return UserRemoteDataSourceImpl(client)
    }

}