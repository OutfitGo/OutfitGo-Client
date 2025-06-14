package com.outfitgo.store.presentation.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.outfitgo.store.R
import com.outfitgo.store.core.util.CurrencyExchange
import com.outfitgo.store.core.util.convertISODateToReadableDate
import com.outfitgo.store.core.util.toCurrency
import com.outfitgo.store.domain.model.order.Order
import com.outfitgo.store.domain.model.order.OrderProduct
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.presentation.brandproducts.components.ProductsPageLoadingState
import com.outfitgo.store.presentation.components.EmptyState
import com.outfitgo.store.presentation.ui.theme.DottedShape
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme
import java.util.Locale

@Composable
fun OrdersSection(
    orders: List<Order>,
    isLoading: Boolean,
    isEndReached: Boolean,
    onRequestNextOrders: () -> Unit,
    onOrderClicked: (Order) -> Unit
) {

    if(orders.isEmpty() && !isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            EmptyState(
                imgRes = R.drawable.empty_orders_img,
                mainText = stringResource(R.string.no_orders_yet),
                description = stringResource(R.string.empty_orders_description)
            )
        }
    }else{
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(items = orders) { index, order ->
                if (index >= orders.size - 1 && !isEndReached && !isLoading) {
                    onRequestNextOrders()
                }

                OrderItem(
                    order = order,
                    onOrderClicked = onOrderClicked
                )
            }

            if (isLoading) {
                items(count = 2) {
                    OrderLoadingItem()
                }
            }
        }
    }

}

@Composable
private fun OrderItem(
    order: Order,
    onOrderClicked: (Order) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onOrderClicked(order)
                }
            )
    ) {
        Text(
            text = stringResource(R.string.order, order.number),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.ordered_at, convertISODateToReadableDate(order.date)),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            Modifier
                .padding(vertical = 8.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    shape = DottedShape(step = 10.dp)
                )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.payment_status),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
            )

            Text(
                text = order.paymentStatus,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.items),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
            )

            Text(
                text = "${order.items.size}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.total_price),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
            )

            Text(
                text = "${order.totalPrice.toCurrency()} ${CurrencyExchange.currentCurrencyUnit}",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp,
            )
        }

        OrderProductsList(
            products = order.items
        )
    }
}

@Composable
private fun OrderProductsList(
    products: List<OrderProduct>
) {
    LazyRow(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = products.take(2)
        ) { product ->
            AsyncImage(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    ),
                model = product.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(R.drawable.product_img_placeholder),
            )
        }

        if (products.size > 2) {
            item {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "...",
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}