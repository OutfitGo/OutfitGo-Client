package com.outfitgo.store.core.util

fun Double.toCurrency(rate: Double): Double {

    return this * CurrencyExchange.rate
}

fun String.toCurrency(): String {
    val converted = this.toDoubleOrNull()?.times(CurrencyExchange.rate) ?: 0.0
    return "%.2f".format(converted)
}

object CurrencyExchange {
    var currentCurrencyUnit = CurrencyUnit.EGP.name
    var rate: Double = 1.0
}