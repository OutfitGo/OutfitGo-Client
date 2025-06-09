package com.outfitgo.store.presentation.wishlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.presentation.components.EmptyState
import com.outfitgo.store.presentation.wishlist.components.WishlistItem
import kotlinx.coroutines.flow.SharedFlow


@Composable
fun WishlistScreen(
    modifier: Modifier = Modifier,
    viewModel: WishlistViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onGoToProductDetails: (productId: String) -> Unit
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    WishlistScreenContent(
        state = state.value,
        onIntent = { intent ->
            when (intent) {
                is WishlistIntent.GoToProductDetails -> onGoToProductDetails(intent.product.id)
                WishlistIntent.NavigateUp -> onNavigateUp()
                else -> viewModel.processIntent(intent)
            }
        },
        effectFlow = viewModel.effect,
        modifier = modifier
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreenContent(
    state: WishlistUiState,
    onIntent: (WishlistIntent) -> Unit,
    effectFlow: SharedFlow<WishlistEffect>,
    modifier: Modifier = Modifier
) {

    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var selectedProductToDelete: Product? by remember { mutableStateOf(null) }


    // effects
    LaunchedEffect(Unit) {
        effectFlow.collect { event ->
            when (event) {
                is WishlistEffect.SendSnackBar -> snackbarHostState.showSnackbar(event.msg)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.wishlist)) },
                navigationIcon = {
                    IconButton(onClick = { onIntent(WishlistIntent.NavigateUp) }) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(
                                R.string.back
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { innerPadding ->

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (state.products.isEmpty()) {
            EmptyState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                imgRes = R.drawable.wishlist_empty,
                mainText = "Empty Wishlist",
                description = "start browsing and add items to wishlist"
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(state.products, key = { it.id }) { product ->
                    WishlistItem(
                        product = product,
                        onRemoveClicked = {
                            showConfirmationDialog = true
                            selectedProductToDelete = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClicked = {
                            onIntent(WishlistIntent.GoToProductDetails(it))
                        }
                    )
                }
            }
        }

        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = {
                    showConfirmationDialog = false
                    selectedProductToDelete = null
                },
                confirmButton = {
                    TextButton(onClick = {
                        onIntent(WishlistIntent.RemoveProduct(selectedProductToDelete ?: Product()))
                        showConfirmationDialog = false
                        selectedProductToDelete = null
                    }) { Text("Remove") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showConfirmationDialog = false
                        selectedProductToDelete = null
                    }) { Text("Cancel") }
                },
                title = {
                    Text("Remove Item From wishlist?")
                },
                text = {
                    Text("you are about to remove item from wishlist, are you sure?")
                }
            )
        }
    }


}

