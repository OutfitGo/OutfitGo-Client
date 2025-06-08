package com.outfitgo.store.data.datasource.local.cart

import kotlinx.coroutines.flow.Flow

interface CartLocalDataSource {
    suspend fun saveCartId(cartId: String)
    suspend fun getCartId(): Flow<String>
}