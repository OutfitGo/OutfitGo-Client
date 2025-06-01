package com.outfitgo.store.data.datasource.local.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.outfitgo.store.core.util.CurrencyUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsLocalDataSourceImpl @Inject constructor(val dataStore: DataStore<Preferences>) :
    SettingsLocalDataSource {

    companion object {
        private val KEY_CURRENCY_UNIT = stringPreferencesKey("currency_unit")
        private val DEFAULT_UNIT = CurrencyUnit.EGP
    }

    override suspend fun saveCurrencyUnit(unit: CurrencyUnit) {
        dataStore.edit { preferences ->
            preferences[KEY_CURRENCY_UNIT] = unit.name
        }
    }

    override fun getCurrencyUnit(): Flow<CurrencyUnit> {
        return dataStore.data.map { preferences ->
            val savedValue = preferences[KEY_CURRENCY_UNIT]
            runCatching { CurrencyUnit.valueOf(savedValue ?: "") }.getOrDefault(DEFAULT_UNIT)
        }
    }

}