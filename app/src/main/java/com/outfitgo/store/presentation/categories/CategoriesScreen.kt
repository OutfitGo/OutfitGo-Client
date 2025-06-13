package com.outfitgo.store.presentation.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.presentation.categories.CategoriesIntent.GoToCategoryProducts
import com.outfitgo.store.presentation.categories.components.CategoryItem

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = hiltViewModel(),
    onNavigateToCategoryProducts: (Collection) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    CategoriesScreenContent(
        state = uiState,
        onEvent = { event ->
            when (event) {
                is GoToCategoryProducts -> onNavigateToCategoryProducts(event.category)
            }
        }
    )
}

@Composable
private fun CategoriesScreenContent(
    state: CategoriesState,
    onEvent: (CategoriesIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.secondary)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.categories),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(items = state.categories) { category ->
                CategoryItem(
                    category = category,
                    onCategoryClicked = { category -> onEvent(GoToCategoryProducts(category = category)) }
                )
            }
        }
    }
}
