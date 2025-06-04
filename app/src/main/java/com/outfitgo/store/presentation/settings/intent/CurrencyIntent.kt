package com.outfitgo.store.presentation.settings.intent

import com.outfitgo.store.core.util.CurrencyUnit

sealed class CurrencyIntent {
    data class SelectCurrency(val currency: CurrencyUnit) : CurrencyIntent()
}