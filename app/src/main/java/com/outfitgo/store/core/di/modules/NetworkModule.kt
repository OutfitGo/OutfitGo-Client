package com.outfitgo.store.core.di.modules

import android.content.Context

import com.outfitgo.store.core.util.ConnectivityManagerNetworkObserver
import com.outfitgo.store.core.util.NetworkObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkObserver(@ApplicationContext context: Context): NetworkObserver {
        return ConnectivityManagerNetworkObserver(context)
    }
}