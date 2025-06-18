package com.outfitgo.store.presentation.brandproducts.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.presentation.components.ProductItem
import com.outfitgo.store.presentation.home.components.ProductsPageLoadingState

@Composable
fun BrandProductsSection(
    products: List<Product>,
    isEndReached: Boolean,
    isLoading: Boolean,
    onProductClicked: (Product) -> Unit,
    onRequestNextProducts: () -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        itemsIndexed(
            items = products
        ) { index, product ->
            if (index >= products.size - 1 && !isEndReached && !isLoading) {
                onRequestNextProducts()
            }

            ProductItem(
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