package com.outfitgo.store.domain.repository.settings

import com.outfitgo.store.core.util.CurrencyUnit
import com.outfitgo.store.domain.model.Currency
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun getLatestExchangeRate(baseCurrency: CurrencyUnit, targetCurrency: CurrencyUnit):Currency
    suspend fun saveCurrencyUnit(unit: CurrencyUnit)
    fun getCurrencyUnit(): Flow<CurrencyUnit>
}