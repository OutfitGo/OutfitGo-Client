package com.outfitgo.store.presentation.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.CurrencyExchange
import com.outfitgo.store.core.util.CurrencyUnit
import com.outfitgo.store.domain.usecase.settings.GetCurrencyUnitUseCase
import com.outfitgo.store.domain.usecase.settings.GetLatestExchangeRateUseCase
import com.outfitgo.store.domain.usecase.settings.SaveCurrencyUnitUseCase
import com.outfitgo.store.presentation.settings.intent.CurrencyIntent
import com.outfitgo.store.presentation.settings.state.CurrencyState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val getCurrencyUnitUseCase: GetCurrencyUnitUseCase, // returns Flow<CurrencyUnit>
    private val getLatestExchangeRateUseCase: GetLatestExchangeRateUseCase,
    private val saveCurrencyUnitUseCase: SaveCurrencyUnitUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CurrencyState())
    val state: StateFlow<CurrencyState> = _state

    init {
        observeSavedCurrency()
    }

    private fun observeSavedCurrency() {
        viewModelScope.launch {
            getCurrencyUnitUseCase.execute()
                .distinctUntilChanged()
                .collectLatest { savedCurrency ->
                    _state.update { it.copy(selectedCurrency = savedCurrency) }
                    fetchExchangeRate(savedCurrency)
                }
        }
    }

    fun processIntent(intent: CurrencyIntent) {
        when (intent) {
            is CurrencyIntent.SelectCurrency -> {
                viewModelScope.launch {
                    saveCurrencyUnitUseCase.execute(intent.currency)
                }
            }
        }
    }

    private suspend fun fetchExchangeRate(currency: CurrencyUnit) {
        val fetchedCurrency = getLatestExchangeRateUseCase.execute(targetCurrency = currency)
        CurrencyExchange.rate=fetchedCurrency.value
        CurrencyExchange.currentCurrencyUnit = fetchedCurrency.code
    }
}
