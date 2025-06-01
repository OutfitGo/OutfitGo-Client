package com.outfitgo.store.data.mappers

import com.outfitgo.store.data.dto.CurrencyDTO
import com.outfitgo.store.domain.model.Currency

fun CurrencyDTO.toCurrency(): Currency {
    return Currency(code = code, value = value)
}