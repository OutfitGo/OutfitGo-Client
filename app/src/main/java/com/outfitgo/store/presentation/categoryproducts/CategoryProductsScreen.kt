package com.outfitgo.store.presentation.categoryproducts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.core.util.enums.ProductType
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.presentation.brandproducts.components.ProductsSearchBar
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.ApplyFilterOptions
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.ChangeProductType
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.GetProducts
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.GoToProductDetails
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.NavigateUp
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.Search
import com.outfitgo.store.presentation.categoryproducts.CategoryProductsIntent.ShowFilterBottomSheet
import com.outfitgo.store.presentation.categoryproducts.components.CategoryProductsHeaderBar
import com.outfitgo.store.presentation.categoryproducts.components.CategoryProductsSection
import com.outfitgo.store.presentation.categoryproducts.components.FiltersBottomSheet
import com.outfitgo.store.presentation.categoryproducts.components.ProductTypeFilterChips

@Composable
fun CategoryProductsScreen(
    viewModel: CategoryProductsViewModel = hiltViewModel(),
    categoryHandle: String,
    categoryName: String,
    onNavigateToProductDetails: (Product) -> Unit,
    onNavigateUp: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    var isFilterBottomSheetShown by remember { mutableStateOf(false) }
    val types =
        listOf(ProductType.ALL, ProductType.SHOES, ProductType.ACCESSORIES, ProductType.T_SHIRTS)

    CategoryProductsScreenContent(
        state = state,
        categoryName = categoryName,
        types = types,
        onEvent = { event ->
            when (event) {
                is NavigateUp -> onNavigateUp()
                is GoToProductDetails -> onNavigateToProductDetails(event.product)
                is ShowFilterBottomSheet -> isFilterBottomSheetShown = true
                else -> viewModel.processIntent(event)
            }
        }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.processIntent(GetProducts(categoryHandle = categoryHandle))
    }

    if (isFilterBottomSheetShown) {
        FiltersBottomSheet(
            initialPriceRange = state.initialPriceRange,
            currentPriceRange = state.filterOptions.priceRange,
            currentSortOption = state.filterOptions.sortOption,
            onApplyFilters = { priceRange, sortOption ->
                viewModel.processIntent(
                    ApplyFilterOptions(
                        priceRange = priceRange,
                        sortOption = sortOption
                    )
                )
            },
            onDismiss = {
                isFilterBottomSheetShown = false
            }
        )
    }
}

@Composable
private fun CategoryProductsScreenContent(
    state: CategoryProductsState,
    categoryName: String,
    types: List<ProductType>,
    onEvent: (CategoryProductsIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(24.dp)
    ) {
        CategoryProductsHeaderBar(
            category = categoryName,
            onBackClicked = { onEvent(NavigateUp) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductsSearchBar(
                modifier = Modifier.weight(1f),
                onQueryChanged = { query ->
                    onEvent(Search(query = query))
                }
            )

            IconButton(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .padding(8.dp),
                onClick = { onEvent(ShowFilterBottomSheet) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.FilterAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        ProductTypeFilterChips(
            types = types,
            onTypeSelected = { type ->
                onEvent(ChangeProductType(productType = type))
            }
        )

        CategoryProductsSection(
            products = state.products,
            isLoading = state.isLoading,
            filterOptions = state.filterOptions,
            onProductClicked = { product ->
                onEvent(GoToProductDetails(product = product))
            }
        )
    }
}