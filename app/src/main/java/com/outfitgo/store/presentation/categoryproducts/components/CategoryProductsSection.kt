package com.outfitgo.store.presentation.categoryproducts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.outfitgo.store.R
import com.outfitgo.store.core.util.enums.ProductType
import com.outfitgo.store.core.util.enums.SortOption
import com.outfitgo.store.domain.model.FilterOptions
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.presentation.components.EmptyState
import com.outfitgo.store.presentation.components.ProductItem
import com.outfitgo.store.presentation.components.shimmerBrush

@Composable
fun CategoryProductsSection(
    products: List<Product>,
    isLoading: Boolean,
    filterOptions: FilterOptions,
    onProductClicked: (Product) -> Unit,
) {
    var filteredProducts = products
        .filter { it.name.contains(filterOptions.searchQuery, ignoreCase = true) }
        .filter { it.price.toFloat() >= filterOptions.priceRange.start && it.price.toFloat() <= filterOptions.priceRange.endInclusive }

    if (filterOptions.productType != ProductType.ALL) {
        filteredProducts = filteredProducts.filter {
            it.type.lowercase() == filterOptions.productType.getName().lowercase()
        }
    }

    val sortedProducts = when (filterOptions.sortOption) {
        SortOption.ALPHABETICAL -> filteredProducts.sortedBy { it.name }
        SortOption.ALPHABETICAL_REVERSED -> filteredProducts.sortedByDescending { it.name }
        SortOption.LOWEST_PRICE -> filteredProducts.sortedBy { it.price }
        SortOption.HIGHEST_PRICE -> filteredProducts.sortedByDescending { it.price }
    }

    if(sortedProducts.isEmpty() && !isLoading){
        EmptyState(
            imgRes = R.drawable.search_empty_img,
            mainText = stringResource(R.string.no_results_found),
            description = stringResource(R.string.search_empty_state_description)
        )
    }else{
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            itemsIndexed(
                items = sortedProducts,
                key = { _, product -> product.id }
            ) { index, product ->
                ProductItem(
                    modifier = Modifier.animateItem(),
                    product = product,
                    onProductClicked = onProductClicked
                )
            }

            if (isLoading) {
                items(count = 4) {
                    ProductsPageLoadingState()
                }
            }
        }
    }

}

@Composable
private fun ProductsPageLoadingState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(0.8f)
                .height(10.dp)
                .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(0.4f)
                .height(10.dp)
                .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
        )

        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(0.6f)
                .height(10.dp)
                .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
        )
    }
}