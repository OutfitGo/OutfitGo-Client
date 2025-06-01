package com.outfitgo.store.domain.usecase.settings


import com.outfitgo.store.core.util.CurrencyUnit
import com.outfitgo.store.domain.repository.settings.SettingsRepository
import javax.inject.Inject

class GetLatestExchangeRateUseCase @Inject constructor(val settingsRepository: SettingsRepository) {
    suspend fun execute(baseCurrency: CurrencyUnit = CurrencyUnit.EGP,targetCurrency: CurrencyUnit) = settingsRepository.getLatestExchangeRate(baseCurrency, targetCurrency)
}