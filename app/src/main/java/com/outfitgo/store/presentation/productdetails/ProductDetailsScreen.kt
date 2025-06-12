import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.outfitgo.store.core.util.CurrencyExchange
import com.outfitgo.store.core.util.toCurrency
import com.outfitgo.store.data.mappers.toProduct
import com.outfitgo.store.domain.model.Review
import com.outfitgo.store.domain.model.ReviewUtils
import com.outfitgo.store.domain.model.product.DetailedProduct
import com.outfitgo.store.domain.model.product.ProductVariant
import com.outfitgo.store.presentation.components.RatingBar
import com.outfitgo.store.presentation.components.ReviewCard
import com.outfitgo.store.presentation.productdetails.ProductDetailsEffect
import com.outfitgo.store.presentation.productdetails.ProductDetailsIntent
import com.outfitgo.store.presentation.productdetails.ProductDetailsState
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

private const val TAG = "ProductDetailsScreen"

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: String,
    state: ProductDetailsState,
    onIntent: (ProductDetailsIntent) -> Unit,
    effect: SharedFlow<ProductDetailsEffect>,
    onNavigateUp: () -> Unit,
    onShowMoreReviewsClicked: (List<Review>) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { state.product.imageUrls.size })
    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmationDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        effect.collect {
            when (it) {
                is ProductDetailsEffect.SendSnackBar -> {
                    snackbarHostState.showSnackbar(it.msg)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        onIntent(ProductDetailsIntent.GetProductById(productId))
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateUp()
                    }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { paddingValues ->

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {

                // Product Images Carousel
                item {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(vertical = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) { page ->
                        // Use AsyncImage to load images from URLs
                        if (state.product.imageUrls.isNotEmpty()) {
                            AsyncImage(
                                model = state.product.imageUrls[page],
                                contentDescription = "Product Image ${page + 1}",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                    }
                    // Page indicator (optional)
                    Row(
                        Modifier
                            .height(20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(pagerState.pageCount) { iteration ->
                            val color =
                                if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                }

                // Product Information
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        Text(state.product.category, modifier = Modifier.alpha(0.8f))
                        Text(
                            text = state.product.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            "Brand: ${state.product.vendor}",
                            fontWeight = Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            state.product.tags.joinToString(" | "),
                            modifier = Modifier.alpha(0.8f)
                        )

                        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(top = 8.dp))
                        Text(
                            text = "Product Description",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = Bold),
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        Text(
                            text = state.product.description,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .alpha(0.8f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            RatingBar(rating = state.product.rating)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${
                                    String.format(
                                        "%.2f",
                                        state.product.rating
                                    )
                                } / ${ReviewUtils.MAX_RATING}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Text(
                            text = "${state.product.price.toCurrency()} ${CurrencyExchange.currentCurrencyUnit}",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                item {
                    Text(
                        text = "Available Variants",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = Bold),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    LazyRow(modifier = Modifier.padding(bottom = 8.dp)) {
                        items(state.product.variants) {
                            FilterChip(
                                selected = it.id == state.selectedVariantId,
                                onClick = { onIntent(ProductDetailsIntent.SelectProductVariant(it)) },
                                label = { Text(it.title) },
                                leadingIcon = {
                                    if (it.id == state.selectedVariantId) Icon(
                                        Icons.Outlined.Check,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                // Reviews Section
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Customer Reviews",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = Bold),
                        )

                        TextButton(onClick = {
                            onShowMoreReviewsClicked(state.product.reviews)
                        }) {
                            Text("More")
                            Icon(
                                Icons.AutoMirrored.Outlined.NavigateNext,
                                contentDescription = "more reviews"
                            )
                        }
                    }
                }

                item {
                    LazyRow(modifier = Modifier.padding(bottom = 8.dp)) {
                        items(state.product.reviews.take(2) ) { review ->
                            ReviewCard(review)
                        }
                    }
                }

                // Buttons (add to cart and add to favorites)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        IconButton(onClick = {
                            if(state.isFavorite) {
                                showConfirmationDialog = true
                            } else {
                                onIntent(ProductDetailsIntent.AddToWishlist(state.product.toProduct()))
                            }
                        }) {
                            Icon(
                                imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Add to Wishlist"
                            )
                        }

                        Button(onClick = {
                            onIntent(ProductDetailsIntent.AddToCart(state.selectedVariantId))
                        }, modifier = Modifier.weight(1f)) {
                            if (state.isAddedToCart) {
                                Text("Already In Cart")
                            } else {
                                Text("Add To Cart")
                            }
                        }
                    }
                }
            }
        }


        if(showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = { Text("Remove ${state.product.title}") },
                text = { Text("Are you sure you want to remove this item from your wishlist?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showConfirmationDialog = false
                            onIntent(ProductDetailsIntent.RemoveFromWishList(state.product.id))
                        }
                    ) {
                        Text("Remove")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmationDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }


}


@Preview(showBackground = true)
@Composable
fun PreviewProductDetailsScreen() {
    OutfitGoTheme {
        val dummyDetailedProduct = DetailedProduct(
            id = "prod_001",
            title = "Stylish Smartwatch X200",
            description = "A sleek and feature-rich smartwatch with health tracking, notifications, and long battery life. Perfect for fitness enthusiasts and tech lovers.",
            price = "249.99",
            imageUrls = listOf(
                "https://images.unsplash.com/photo-1546868870-7607a7509f7a?q=80&w=2000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", // Example: Smartwatch on wrist
                "https://images.unsplash.com/photo-1579586326442-f0450529d33b?q=80&w=2000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", // Example: Smartwatch close-up
                "https://images.unsplash.com/photo-1585973715104-e3dc1ee9a099?q=80&w=2000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"  // Example: Smartwatch in box
            ),
            tags = listOf("wearable", "electronics", "fitness", "smartwatch"),
            vendor = "TechGadgets Inc.",
            category = "Electronics",
            rating = 4.7,
            reviews = listOf(
                Review(
                    id = "rev_001",
                    reviewerName = "Alice Smith",
                    rating = 5.0,
                    comment = "Amazing product! The battery life is incredible and it's super accurate.",
                    dateString = "2024-05-15"
                ),
                Review(
                    id = "rev_002",
                    reviewerName = "Bob Johnson",
                    rating = 4.5,
                    comment = "Great smartwatch for the price. The app could be a bit more user-friendly.",
                    dateString = "2024-05-20"
                )
            ),
            currencyCode = "USD",
            variants = listOf(
                ProductVariant(id = "sdlfkjsdl", title = "4 / burgandy"),
                ProductVariant(id = "sdlfkjsdl", title = "5 / burgandy"),
                ProductVariant(id = "sdlfkjsdl", title = "10 / burgandy"),
                ProductVariant(id = "sdlfkjsdl", title = "8 / burgandy"),
            )
        )

        ProductDetailsScreen(
            productId = dummyDetailedProduct.id,
            state = ProductDetailsState(product = dummyDetailedProduct),
            onIntent = { },
            effect = MutableSharedFlow(),
            modifier = Modifier.fillMaxSize(),
            onNavigateUp = {},
            onShowMoreReviewsClicked = {}
        )
    }

}

