package com.outfitgo.store.presentation.brandproducts.components

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
import androidx.compose.ui.unit.dp
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.presentation.components.ProductItem
import com.outfitgo.store.presentation.components.shimmerBrush

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

@Composable
fun ProductsPageLoadingState() {
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