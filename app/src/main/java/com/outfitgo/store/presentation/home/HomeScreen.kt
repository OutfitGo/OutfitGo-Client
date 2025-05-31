package com.outfitgo.store.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.presentation.home.components.BrandsSection
import com.outfitgo.store.presentation.home.components.CouponAdsSection
import com.outfitgo.store.presentation.home.components.HomeHeaderBar
import com.outfitgo.store.presentation.home.components.NewArrivalSection

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearchScreen: () -> Unit = {},
    onNavigateToBrandProducts: (String) -> Unit = {},
    onNavigateToProductDetails: (CommonProduct) -> Unit = {}
) {
    val homeState = viewModel.homeState.collectAsStateWithLifecycle()

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

                else -> viewModel.processIntent(event)
            }
        }
    )
}

@Composable
private fun HomeScreenContent(
    homeState: HomeState,
    onEvent: (HomeIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(top = 42.dp, bottom = 24.dp)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        HomeHeaderBar(
            onSearchClicked = {
                onEvent(HomeIntent.GoToSearch)
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        CouponAdsSection()

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        BrandsSection(
            brands = homeState.brands,
            isLoading = homeState.isBrandsLoading,
            isEndReached = homeState.brandEndReached,
            onRequestNextBrands = {
                onEvent(HomeIntent.GetNextBrands)
            },
            onBrandClicked = { brand ->
                onEvent(HomeIntent.GoToBrandProducts(brand.name))
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        NewArrivalSection(
            products = homeState.latestProducts,
            isLoading = homeState.isLatestProductsLoading,
            isEndReached = homeState.latestProductsEndReached,
            onRequestNextProducts = {
                onEvent(HomeIntent.GetNextLatestProducts)
            },
            onProductClicked = {
                onEvent(HomeIntent.GoToProductDetails(it))
            }
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    //HomeScreenContent()
}