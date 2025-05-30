package com.outfitgo.store.data.datasource.local.settings

import com.outfitgo.store.core.util.CurrencyUnit
import kotlinx.coroutines.flow.Flow

interface SettingsLocalDataSource {
    suspend fun saveCurrencyUnit(unit: CurrencyUnit)
    fun getCurrencyUnit(): Flow<CurrencyUnit>
}