package com.outfitgo.store.core.di.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.BuildConfig
import com.outfitgo.store.core.di.qualifiers.AdminApollo
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.domain.repository.user.UsersRepository
import com.outfitgo.store.domain.usecase.auth.LoginWithEmailAndPasswordUseCase
import com.outfitgo.store.presentation.settings.viewModel.CurrencyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    const val SERVER_URL = "https://mad-and2-sv.myshopify.com/api/2025-04/graphql.json"
    const val ADMIN_SERVER_URL =
        "https://mad-and2-sv.myshopify.com/admin/api/2025-04/graphql.json"
    private const val DATASTORE_NAME = "OutfitGo"


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

    @Provides
    @Singleton
    fun provideClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    fun provideLoginWithEmailAndPasswordUseCase(usersRepository: UsersRepository): LoginWithEmailAndPasswordUseCase {
        return LoginWithEmailAndPasswordUseCase(usersRepository)
    }


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(DATASTORE_NAME)
            }
        )
    }
    @Provides
    @Singleton
    fun provideCurrencyManager(): CurrencyManager {
        return CurrencyManager()
    }
}