package com.outfitgo.store.presentation.categoryproducts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.outfitgo.store.R
import com.outfitgo.store.core.util.enums.SortOption
import com.outfitgo.store.core.util.enums.SortOption.ALPHABETICAL
import com.outfitgo.store.core.util.enums.SortOption.ALPHABETICAL_REVERSED
import com.outfitgo.store.core.util.enums.SortOption.HIGHEST_PRICE
import com.outfitgo.store.core.util.enums.SortOption.LOWEST_PRICE
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    initialPriceRange: ClosedFloatingPointRange<Float>,
    currentPriceRange: ClosedFloatingPointRange<Float>,
    currentSortOption: SortOption,
    onApplyFilters: (priceRange: ClosedFloatingPointRange<Float>, sortOption: SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sliderPosition by remember { mutableStateOf(currentPriceRange) }
    val sortOptions = remember {
        listOf(
            ALPHABETICAL,
            ALPHABETICAL_REVERSED,
            LOWEST_PRICE,
            HIGHEST_PRICE
        )
    }

    var selectedSortOption by remember { mutableStateOf(currentSortOption) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.price_range)
                )

                //TODO Handle Currency Here
                Text(
                    text = "${sliderPosition.start.toInt()} - ${sliderPosition.endInclusive.toInt()}",
                )
            }

            RangeSlider(
                modifier = Modifier.padding(vertical = 8.dp),
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = initialPriceRange,
            )

            Text(
                text = stringResource(R.string.sort_by)
            )

            LazyVerticalGrid(
                modifier = Modifier.padding(top = 8.dp),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = sortOptions) { option ->
                    FilterChip(
                        selected = option == selectedSortOption,
                        onClick = {
                            selectedSortOption = option
                        },
                        label = {
                            Text(text = option.getName())
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                onClick = {
                    onApplyFilters(sliderPosition, selectedSortOption)
                    scope.launch {
                        sheetState.hide()
                        onDismiss()
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.apply)
                )
            }
        }
    }
}

