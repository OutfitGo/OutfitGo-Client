package com.outfitgo.store.data.repository.settings

import com.outfitgo.store.core.util.CurrencyUnit
import com.outfitgo.store.data.datasource.local.settings.SettingsLocalDataSource
import com.outfitgo.store.data.datasource.remote.settings.SettingsRemoteDataSource
import com.outfitgo.store.domain.model.Currency
import com.outfitgo.store.domain.repository.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    val remote: SettingsRemoteDataSource,
    val local: SettingsLocalDataSource
) :SettingsRepository {
    override suspend fun getLatestExchangeRate(baseCurrency: CurrencyUnit, targetCurrency: CurrencyUnit): Currency {
        return remote.getLatestExchangeRate(baseCurrency, targetCurrency)
    }

    override suspend fun saveCurrencyUnit(unit: CurrencyUnit) {
        local.saveCurrencyUnit(unit)
    }

    override fun getCurrencyUnit(): Flow<CurrencyUnit> {
        return local.getCurrencyUnit()
    }
}