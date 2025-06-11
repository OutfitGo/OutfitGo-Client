package com.outfitgo.store.data.datasource.local.cart

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.outfitgo.store.core.util.CurrencyUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartLocalDataSourceImpl @Inject constructor(private val dataStore: DataStore<Preferences>) :
    CartLocalDataSource {
    companion object {
        private val KEY_CART_ID = stringPreferencesKey("CART_ID")
    }

    override suspend fun saveCartId(cartId: String) {
        dataStore.edit { preferences ->
            preferences[KEY_CART_ID] = cartId
        }
    }

    override suspend fun getCartId(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[KEY_CART_ID] ?: ""
        }
    }
}