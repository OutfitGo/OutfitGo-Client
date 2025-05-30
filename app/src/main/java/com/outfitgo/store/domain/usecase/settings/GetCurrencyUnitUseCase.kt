package com.outfitgo.store.domain.usecase.settings

import com.outfitgo.store.domain.repository.settings.SettingsRepository
import javax.inject.Inject

class GetCurrencyUnitUseCase @Inject constructor(private val settingsRepository: SettingsRepository) {
    fun execute() = settingsRepository.getCurrencyUnit()
}