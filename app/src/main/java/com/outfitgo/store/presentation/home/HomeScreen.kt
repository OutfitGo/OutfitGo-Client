package com.outfitgo.store.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.presentation.components.ProductItem
import com.outfitgo.store.presentation.home.components.BrandsSection
import com.outfitgo.store.presentation.home.components.CouponAdsSection
import com.outfitgo.store.presentation.home.components.HomeHeaderBar
import com.outfitgo.store.presentation.home.components.ProductsPageLoadingState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToSettingsScreen: () -> Unit,
    onNavigateToBrandProducts: (String) -> Unit,
    onNavigateToProductDetails: (Product) -> Unit,
) {
    val homeState = viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        homeState = homeState.value,
        onEvent = { event ->
            when (event) {
                is HomeIntent.GoToBrandProducts -> {
                    onNavigateToBrandProducts(event.brand)
                }

                is HomeIntent.GoToProductDetails -> {
                    onNavigateToProductDetails(event.product)
                }

                is HomeIntent.GoToSearch -> onNavigateToSearchScreen()
                is HomeIntent.GoToSettings -> onNavigateToSettingsScreen()
                else -> viewModel.processIntent(event)
            }
        }
    )
}

@Composable
private fun HomeScreenContent(
    homeState: HomeState,
    onEvent: (HomeIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        HomeHeaderBar(
            onSearchClicked = {
                onEvent(HomeIntent.GoToSearch)
            },
            onSettingsClicked = {
                onEvent(HomeIntent.GoToSettings)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(
                span = { GridItemSpan(2) }
            ) {
                CouponAdsSection(
                    isLoading = homeState.isCouponsLoading,
                    coupons = homeState.coupons
                )
            }

            item(
                span = { GridItemSpan(2) }
            ) {
                BrandsSection(
                    brands = homeState.brands,
                    isLoading = homeState.isBrandsLoading,
                    onBrandClicked = { brand ->
                        onEvent(HomeIntent.GoToBrandProducts(brand.name))
                    }
                )
            }

            item(
                span = { GridItemSpan(2) }
            ){
                Text(
                    text = stringResource(R.string.new_arrival),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            itemsIndexed(
                items = homeState.latestProducts
            ) { index, product ->
                if (index >= homeState.latestProducts.size - 1 && !homeState.latestProductsEndReached && !homeState.isLatestProductsLoading) {
                    onEvent(HomeIntent.GetNextLatestProducts)
                }

                ProductItem(
                    product = product,
                    onProductClicked = {
                        onEvent(HomeIntent.GoToProductDetails(it))
                    }
                )
            }

            if (homeState.isLatestProductsLoading) {
                items(count = 4) {
                    ProductsPageLoadingState()
                }
            }
        }
    }
}