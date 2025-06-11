package com.outfitgo.store.core.di.modules

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.core.di.qualifiers.AdminApollo
import com.outfitgo.store.core.di.qualifiers.StorefrontApollo
import com.outfitgo.store.data.datasource.remote.coupon.CouponRemoteDataSource
import com.outfitgo.store.data.datasource.remote.coupon.CouponRemoteDataSourceImpl
import com.outfitgo.store.data.repository.coupons.CouponsRepositoryImpl
import com.outfitgo.store.domain.repository.coupon.CouponsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CouponModule {
    @Provides
    @Singleton
    fun provideCouponRemoteDataSource(@AdminApollo remoteClient: ApolloClient):CouponRemoteDataSource{
        return CouponRemoteDataSourceImpl(remoteClient)
    }
    @Provides
    @Singleton
    fun provideCouponRepository(remoteDataSource: CouponRemoteDataSource):CouponsRepository{
        return CouponsRepositoryImpl(remoteDataSource)
    }
}