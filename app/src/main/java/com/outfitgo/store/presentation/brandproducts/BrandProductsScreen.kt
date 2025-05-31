package com.outfitgo.store.presentation.brandproducts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.presentation.brandproducts.components.BrandProductsHeaderBar
import com.outfitgo.store.presentation.brandproducts.components.BrandProductsSection
import com.outfitgo.store.presentation.brandproducts.components.ProductsSearchBar
import com.outfitgo.store.presentation.components.EmptyState

@Composable
fun BrandProductsScreen(
    brand: String,
    viewModel: BrandProductsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onNavigateToProductDetails: (CommonProduct) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    BrandProductsScreenContent(
        brand = brand,
        state = uiState.value,
        onEvent = { event ->
            when (event) {
                is BrandProductsIntent.GoBack -> onNavigateUp()
                is BrandProductsIntent.GoToProductDetails -> {
                    onNavigateToProductDetails(event.product)
                }

                else -> viewModel.processIntent(event)
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.setBrandName(brandName = brand)
        viewModel.getNextBrandProducts()
    }
}

@Composable
private fun BrandProductsScreenContent(
    brand: String,
    state: BrandProductsState,
    onEvent: (BrandProductsIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(top = 42.dp, bottom = 24.dp)
            .padding(horizontal = 24.dp)
    ) {
        BrandProductsHeaderBar(
            brand = brand,
            onBackClicked = {
                onEvent(BrandProductsIntent.GoBack)
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        ProductsSearchBar(
            onQueryChanged = { query ->
                onEvent(BrandProductsIntent.ChangeSearchQuery(query = query))
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        BrandProductsSection(
            products = state.products,
            isLoading = state.isLoading,
            isEndReached = state.productsEndReached,
            onRequestNextProducts = {
                onEvent(BrandProductsIntent.GetNextProducts)
            },
            onProductClicked = { product ->
                onEvent(BrandProductsIntent.GoToProductDetails(product = product))
            }
        )

        if(state.products.isEmpty() && !state.isLoading){
            EmptyState(
                imgRes = R.drawable.search_empty_img,
                mainText = "No Results Found",
                description = "Try checking your spelling or using different keywords. We couldn’t find any products matching your search."
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun BrandProductsScreenPreview() {
    BrandProductsScreenContent(
        brand = "Nike",
        state = BrandProductsState(),
        onEvent = {}
    )
}