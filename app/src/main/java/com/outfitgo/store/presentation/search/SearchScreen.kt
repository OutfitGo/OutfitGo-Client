package com.outfitgo.store.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.R
import com.outfitgo.store.core.util.CurrencyExchange
import com.outfitgo.store.presentation.brandproducts.components.ProductsPageLoadingState
import com.outfitgo.store.presentation.brandproducts.components.ProductsSearchBar
import com.outfitgo.store.presentation.components.EmptyState
import com.outfitgo.store.presentation.components.ProductItem

private const val TAG = "SearchScreen"
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {},
    onNavigateToProductDetails: (productId: String) -> Unit = {}
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val searchState = viewModel.searchState.collectAsStateWithLifecycle()
    SearchScreenContents(
        state = state.value,
        onEvent = { event ->
            when (event) {
                SearchScreenIntent.GoBack -> onNavigateUp()
                is SearchScreenIntent.GoToProductDetails -> onNavigateToProductDetails(event.productId)
                else -> viewModel.processIntent(event)
            }
        },
        searchState = searchState.value,
        modifier = modifier.padding(horizontal = 16.dp)
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContents(
    state: SearchScreenUiState,
    searchState: SearchUiState,
    onEvent: (SearchScreenIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {
                        onEvent(SearchScreenIntent.GoBack)
                    }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ProductsSearchBar(
                onQueryChanged = { query ->
                    onEvent(SearchScreenIntent.SearchTitleChanged(query))
                },
                modifier = Modifier.fillMaxWidth()
            )

            RangeSlider(
                value = searchState.range,
                onValueChange = {
                    onEvent(SearchScreenIntent.SearchRangeChanged(it))
                    onEvent(SearchScreenIntent.FilterProductsByRange(it))
                },
                valueRange = searchState.maxRange
            )

            Text(
                stringResource(
                    R.string.from_to, String.format(
                        "%.2f",
                        searchState.range.start
                    ), CurrencyExchange.currentCurrencyUnit, String.format(
                        "%.2f",
                        searchState.range.endInclusive
                    ), CurrencyExchange.currentCurrencyUnit
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )

            if (state.products.isEmpty() && !state.isLoading) {
                EmptyState(
                    imgRes = R.drawable.search_empty_img,
                    mainText = stringResource(R.string.no_results_found),
                    description = stringResource(R.string.try_checking_your_spelling_or_using_different_keywords_we_couldn_t_find_any_products_matching_your_search)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    itemsIndexed(
                        items = state.products, key = { index, product -> product.id }
                    ) { index, product ->

                        ProductItem(
                            product = product,
                            onProductClicked = {
                                onEvent(SearchScreenIntent.GoToProductDetails(product.id))
                            },
                            modifier = Modifier.animateItem()
                        )
                    }

                    if (state.isLoading) {
                        items(count = 4) {
                            ProductsPageLoadingState()
                        }
                    }
                }
            }
        }
    }
}