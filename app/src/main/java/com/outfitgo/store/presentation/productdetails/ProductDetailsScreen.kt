import android.util.Log
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
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.outfitgo.store.domain.model.Review
import com.outfitgo.store.domain.model.ReviewUtils
import com.outfitgo.store.presentation.productdetails.ProductDetailsEffect
import com.outfitgo.store.presentation.productdetails.ProductDetailsIntent
import com.outfitgo.store.presentation.productdetails.ProductDetailsState
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme
import kotlinx.coroutines.flow.SharedFlow

private const val TAG = "ProductDetailsScreen"

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: String,
    state: ProductDetailsState,
    onIntent: (ProductDetailsIntent) -> Unit,
    effect: SharedFlow<ProductDetailsEffect>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { state.product.imageUrls.size })
    var showAllReviews by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(Unit) {
        effect.collect {
            when (it) {
                ProductDetailsEffect.GoToReviewsScreen -> {
                    Log.i(TAG, "ProductDetailsScreen: Navigation to ReviewsScreen")
                }

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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Product Images Carousel
            item {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(16.dp)
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
                        .padding(16.dp)
                ) {
                    Text(
                        text = state.product.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = state.product.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 8.dp)
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
                        text = "${state.product.currencyCode} ${state.product.price}",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }

            // Reviews Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Customer Reviews",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    TextButton(onClick = {
                        onIntent(ProductDetailsIntent.ShowAllReviews)
                    }) {
                        Text("More")
                        Icon(Icons.AutoMirrored.Outlined.NavigateNext, contentDescription = "more reviews")
                    }
                }
            }

            val reviewsToShow =
                if (showAllReviews) state.product.reviews else state.product.reviews.take(2) // Show first 2 reviews initially
            item {
                LazyRow {
                    items(reviewsToShow) { review ->
                        ReviewCard(review)
                    }
                }
            }

            item {
                Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)){
                    IconButton(onClick = {
                        onIntent(ProductDetailsIntent.AddToWishlist(productId))
                    }) {
                        Icon(
                            imageVector = if (state.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Add to Wishlist"
                        )
                    }

                    Button(onClick = {
                        onIntent(ProductDetailsIntent.AddToCart(productId))
                    }, modifier = Modifier.weight(1f)) {
                        Text("Add To Cart")
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = review.reviewerName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = review.dateString,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            RatingBar(rating = review.rating, starSize = 16.dp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Composable for a generic rating bar (remains the same)
@Composable
fun RatingBar(rating: Double, maxRating: Int = 5, starSize: Dp = 20.dp) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = "Star",
                tint = if (i <= rating) Color(0xFFFFC107) else Color.Gray, // Amber for filled, Gray for empty
                modifier = Modifier.size(starSize)
            )
        }
    }
}

// Preview function (Updated to use GraphQL response structure)
@Preview(showBackground = true)
@Composable
fun PreviewProductDetailsScreen() {
    OutfitGoTheme {

    }
}

