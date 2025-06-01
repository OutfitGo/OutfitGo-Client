package com.outfitgo.store.presentation.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.outfitgo.store.R
import com.outfitgo.store.core.util.CurrencyUnit
import com.outfitgo.store.presentation.settings.intent.CurrencyIntent
import com.outfitgo.store.presentation.settings.viewModel.CurrencyViewModel

@Composable
fun CurrencyScreen(
    viewModel: CurrencyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = stringResource(R.string.select_currency), style = MaterialTheme.typography.titleLarge)

        CurrencyUnit.entries.forEach { currency ->
            CurrencyRadioButton(
                currency = currency,
                selected = state.selectedCurrency == currency,
                onSelect = {
                    viewModel.processIntent(CurrencyIntent.SelectCurrency(it))
                }
            )
        }
    }
}

@Composable
fun CurrencyRadioButton(
    currency: CurrencyUnit,
    selected: Boolean,
    onSelect: (CurrencyUnit) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        RadioButton(
            selected = selected,
            onClick = { onSelect(currency) }
        )
        Text(
            text = currency.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
