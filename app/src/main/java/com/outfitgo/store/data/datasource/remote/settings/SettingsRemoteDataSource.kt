package com.outfitgo.store.data.datasource.remote.settings

import com.outfitgo.store.core.util.CurrencyUnit
import com.outfitgo.store.domain.model.Currency

interface SettingsRemoteDataSource {
    suspend fun getLatestExchangeRate(baseCurrency: CurrencyUnit, targetCurrency: CurrencyUnit): Currency
}