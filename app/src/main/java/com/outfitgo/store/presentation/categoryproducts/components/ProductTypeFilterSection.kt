package com.outfitgo.store.presentation.categoryproducts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.outfitgo.store.core.util.enums.ProductType

@Composable
fun ProductTypeFilterChips(
    types: List<ProductType>,
    onTypeSelected: (ProductType) -> Unit
) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    LazyRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(types) { index, type ->
            FilterChip(
                selected = selectedIndex == index,
                onClick = {
                    selectedIndex = index
                    onTypeSelected(type)
                },
                label = { Text(type.getName()) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}