package com.outfitgo.store.presentation.orderdetails

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.outfitgo.store.R
import com.outfitgo.store.core.util.CurrencyExchange
import com.outfitgo.store.core.util.toCurrency
import com.outfitgo.store.domain.model.order.Order
import com.outfitgo.store.domain.model.order.OrderShippingAddress
import com.outfitgo.store.presentation.components.AppScreenHeader
import com.outfitgo.store.presentation.orderdetails.components.ContactInformationSection
import com.outfitgo.store.presentation.orderdetails.components.OrderItem
import com.outfitgo.store.presentation.orderdetails.components.OrderSummerySection
import com.outfitgo.store.presentation.orderdetails.components.ShippingAddressSection
import com.outfitgo.store.presentation.ui.theme.DottedShape

@Composable
fun OrderDetailsScreen(
    order: Order,
    onNavigateUp: () -> Unit,
    onNavigateToProductDetails: (String) -> Unit,
) {
    OrderDetailsScreenContent(
        order = order,
        onEvent = { event ->
            when (event) {
                is OrderDetailsIntent.GoBack -> onNavigateUp()
                is OrderDetailsIntent.NavigateToProductDetails -> onNavigateToProductDetails(event.productId)
            }
        }
    )
}

@Composable
fun OrderDetailsScreenContent(
    order: Order,
    onEvent: (OrderDetailsIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {

        AppScreenHeader(
            title = stringResource(R.string.order_details),
            onBackClicked = { onEvent(OrderDetailsIntent.GoBack) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OrderSummerySection(
                    orderNumber = order.number,
                    orderDate = order.date,
                    itemsCount = order.items.size,
                    paymentStatus = order.paymentStatus
                )
            }

            item {
                ShippingAddressSection(
                    shippingAddress = order.shippingAddress ?: return@item
                )
            }

            item {
                ContactInformationSection(
                    contactInfo = order.contactInfo
                )
            }

            item {
                Text(
                    text = stringResource(R.string.order_items, order.items.size),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            items(items = order.items) { item ->
                OrderItem(
                    item = item,
                    onItemClicked = { onEvent(OrderDetailsIntent.NavigateToProductDetails(it)) }
                )
            }

            item {
                Column {
                    Box(
                        Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = DottedShape(step = 10.dp)
                            )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.total),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = "${order.totalPrice.toCurrency()} ${CurrencyExchange.currentCurrencyUnit}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            item {
                val context = LocalContext.current
                Button(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW).apply {
                                data = order.trackingUrl.toUri()
                            }
                        )
                    },
                ) {
                    Text(text = stringResource(R.string.see_order_status))
                }
            }
        }
    }
}