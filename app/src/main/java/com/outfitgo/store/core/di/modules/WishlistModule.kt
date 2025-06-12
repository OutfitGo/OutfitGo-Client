package com.outfitgo.store.core.di.modules

import com.google.firebase.firestore.FirebaseFirestore
import com.outfitgo.store.data.datasource.remote.wishlist.WishlistRemoteDataSource
import com.outfitgo.store.data.datasource.remote.wishlist.WishlistRemoteDataSourceImpl
import com.outfitgo.store.data.repository.wishlist.WishlistRepositoryImpl
import com.outfitgo.store.domain.repository.wishilst.WishlistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WishlistModule {

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideWishlistRemoteDataSource(fireStore: FirebaseFirestore): WishlistRemoteDataSource {
        return WishlistRemoteDataSourceImpl(fireStore)
    }

    @Provides
    @Singleton
    fun provideWishlistRepository(remoteDataSource: WishlistRemoteDataSource): WishlistRepository {
        return WishlistRepositoryImpl(remoteDataSource)
    }
}