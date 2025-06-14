package com.outfitgo.store.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.outfitgo.store.R
import com.outfitgo.store.core.util.CurrencyExchange
import com.outfitgo.store.core.util.toCurrency
import com.outfitgo.store.domain.model.cart.CartItem
import com.outfitgo.store.domain.model.cart.Cost
import com.outfitgo.store.presentation.cart.components.PromoCodeInput
import com.outfitgo.store.presentation.components.EmptyState
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel(),
    onCheckout: (String)->Unit,
    modifier: Modifier = Modifier
) {
    val cartState = viewModel.cartState.collectAsStateWithLifecycle()
    val showRemoveDialog = remember { mutableStateOf(false) }
    val itemToRemove = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CartEffect.ShowRemoveItemWarning -> {
                    showRemoveDialog.value = true
                    itemToRemove.value = effect.lineId
                }
            }
        }
    }

    if (showRemoveDialog.value) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog.value = false },
            title = { Text("Remove Item") },
            text = { Text("Are you sure you want to remove this item from your cart?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRemoveDialog.value = false
                        viewModel.processIntent(CartIntent.RemoveItem(itemToRemove.value))
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    if (cartState.value.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            CartScreenContent(
                cartItems = cartState.value.cartItems,
                onIncreaseQuantity = { id, quantity ->
                    viewModel.processIntent(
                        CartIntent.IncreaseItemQuantity(
                            id,
                            quantity
                        )
                    )
                },
                onDecreaseQuantity = { id, quantity ->
                    viewModel.processIntent(
                        CartIntent.DecreaseItemQuantity(
                            id,
                            quantity
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 200.dp)
            )

            CartCostSection(
                cost = cartState.value.cartCost,
                couponCode = cartState.value.coupon,
                isCouponApplied = cartState.value.isCouponApplied,
                couponMessage = cartState.value.couponMessage,
                onCouponCodeChange = { viewModel.processIntent(CartIntent.UpdateCouponCode(it)) },
                onApplyCouponClick = { viewModel.processIntent(CartIntent.ApplyCoupon) },
                onContinueClick = { onCheckout(cartState.value.checkoutUrl) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

}

@Composable
private fun CartScreenContent(
    cartItems: List<CartItem>,
    onIncreaseQuantity: (String, Int) -> Unit,
    onDecreaseQuantity: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        CartHeaderSection()
        CartItemsListSection(cartItems, onIncreaseQuantity, onDecreaseQuantity)
        Spacer(modifier = Modifier.weight(1f))

    }
}

@Composable
fun CartHeaderSection(modifier: Modifier = Modifier) {
    Text(
        stringResource(R.string.cart),
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier
    )
}

@Composable
fun CartItemsListSection(
    cartItems: List<CartItem>,
    onIncreaseQuantity: (String, Int) -> Unit,
    onDecreaseQuantity: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        if (cartItems.isNotEmpty()) {
            items(cartItems, key = { it.id }) { item ->

                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        if (value == SwipeToDismissBoxValue.EndToStart) {
                            onDecreaseQuantity(item.id, 1)
                            true
                        } else {
                            false
                        }
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.errorContainer)
                                .padding(horizontal = 24.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                ) {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                    ){
                        CartItemRow(
                            cartItem = item,
                            addQuantityAction = onIncreaseQuantity,
                            removeItemAction = onDecreaseQuantity
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    }
                }
                LaunchedEffect(dismissState.currentValue) {
                    if (dismissState.currentValue != SwipeToDismissBoxValue.EndToStart &&
                        dismissState.targetValue == SwipeToDismissBoxValue.Settled
                    ) {
                        dismissState.reset()
                    }
                }
            }
        } else {
            item {
                EmptyState(
                    imgRes = R.drawable.ic_empty_cart,
                    mainText = "Cart is empty",
                    description = "Add some items to cart"
                )
            }
        }
    }
}

@Composable
fun CartItemRow(
    cartItem: CartItem,
    addQuantityAction: (String, Int) -> Unit,
    removeItemAction: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cartItem.merchandise.img,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(75.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = cartItem.merchandise.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${cartItem.merchandise.price.toCurrency()} ${CurrencyExchange.currentCurrencyUnit}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        CartItemQuantity(
            merchandiseQuantity = cartItem.quantity.toString(),
            addQuantityAction = { addQuantityAction(cartItem.id, cartItem.quantity) },
            removeItemAction = { removeItemAction(cartItem.id, cartItem.quantity) }
        )
    }
}

@Composable
fun CartItemQuantity(
    merchandiseQuantity: String,
    addQuantityAction: () -> Unit,
    removeItemAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .height(32.dp)
    ) {
        Box(
            modifier = Modifier
                .clickable { removeItemAction() }
                .background(Color(0xFFE0E0E0))
                .fillMaxHeight()
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "-",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .width(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = merchandiseQuantity,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 50.dp, bottomEnd = 50.dp))
                .clickable { addQuantityAction() }
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun CartCostSection(
    cost: Cost,
    couponCode: String,
    isCouponApplied: Boolean,
    couponMessage: String?,
    onCouponCodeChange: (String) -> Unit,
    onApplyCouponClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .padding(16.dp)
    ) {
        PromoCodeInput(
            promoCode = couponCode,
            onCodeChange = onCouponCodeChange,
            onApply = onApplyCouponClick
        )

        if (!couponMessage.isNullOrBlank()) {
            Text(
                text = couponMessage,
                color = if (isCouponApplied)
                    Color(0xFF2E7D32) else Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${cost.totalAmount.toCurrency()} ${CurrencyExchange.currentCurrencyUnit}",
                style = MaterialTheme.typography.titleMedium
            )
        }

        Button(
            onClick = onContinueClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue to Purchase")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CartScreenPreview() {
    OutfitGoTheme {
        CartScreen(onCheckout = {})
    }
}