package com.outfitgo.store.core.di.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.outfitgo.store.data.datasource.local.settings.SettingsLocalDataSource
import com.outfitgo.store.data.datasource.local.settings.SettingsLocalDataSourceImpl
import com.outfitgo.store.data.datasource.remote.settings.SettingsRemoteDataSource
import com.outfitgo.store.data.datasource.remote.settings.SettingsRemoteDataSourceImpl
import com.outfitgo.store.data.repository.settings.SettingsRepositoryImpl
import com.outfitgo.store.domain.repository.settings.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {
    @Provides
    @Singleton
    fun provideSettingsLocalDataSource(dataStore: DataStore<Preferences>): SettingsLocalDataSource {
        return SettingsLocalDataSourceImpl(dataStore)
    }
    @Provides
    @Singleton
    fun provideSettingsRemoteDataSource(client: HttpClient):SettingsRemoteDataSource{
        return SettingsRemoteDataSourceImpl(client)
    }
    @Provides
    @Singleton
    fun provideSettingsRepository(settingsRemoteDataSource: SettingsRemoteDataSource,settingsLocalDataSource: SettingsLocalDataSource):SettingsRepository{
        return SettingsRepositoryImpl(settingsRemoteDataSource,settingsLocalDataSource)
    }

}