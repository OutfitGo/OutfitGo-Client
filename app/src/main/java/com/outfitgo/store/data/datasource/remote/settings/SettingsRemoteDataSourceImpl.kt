package com.outfitgo.store.data.datasource.remote.settings

import com.outfitgo.store.BuildConfig
import com.outfitgo.store.core.util.Const
import com.outfitgo.store.core.util.CurrencyUnit
import com.outfitgo.store.data.dto.CurrencyDTO
import com.outfitgo.store.data.dto.CurrencyResponseDTO
import com.outfitgo.store.data.mappers.toCurrency
import com.outfitgo.store.domain.model.Currency
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import javax.inject.Inject

class SettingsRemoteDataSourceImpl @Inject constructor(private val client: HttpClient) :SettingsRemoteDataSource {
    override suspend fun getLatestExchangeRate(
        baseCurrency: CurrencyUnit,
        targetCurrency: CurrencyUnit
    ): Currency {
        return client.get(Const.CURRENCY_API_URL) {
            headers {
                append("apikey", BuildConfig.CURRENCY_API_KEY)
            }
            url {
                parameters.append("base_currency", baseCurrency.name)
                parameters.append("currencies", targetCurrency.name)
            }
        }.body<CurrencyResponseDTO>().data[targetCurrency.name]?.toCurrency()
            ?: throw IllegalStateException("Currency '$targetCurrency' not found in response")
    }
}