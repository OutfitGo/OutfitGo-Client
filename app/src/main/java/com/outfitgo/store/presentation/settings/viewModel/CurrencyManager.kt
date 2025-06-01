package com.outfitgo.store.presentation.settings.viewModel

import com.outfitgo.store.core.util.CurrencyUnit
import com.outfitgo.store.domain.model.Currency
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@Singleton
class CurrencyManager @Inject constructor() {

    private val _currentCurrency = MutableStateFlow(Currency(CurrencyUnit.EGP.name,1.0))
    val currentCurrency: StateFlow<Currency> = _currentCurrency.asStateFlow()

    fun updateCurrency(currency: Currency){
        _currentCurrency.value = currency
    }
}