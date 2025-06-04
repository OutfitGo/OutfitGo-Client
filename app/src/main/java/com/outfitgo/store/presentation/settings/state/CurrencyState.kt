package com.outfitgo.store.presentation.settings.state

import com.outfitgo.store.core.util.CurrencyUnit

data class CurrencyState(
    val selectedCurrency: CurrencyUnit = CurrencyUnit.EGP
)