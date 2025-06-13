package com.outfitgo.store.presentation.orderdetails.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outfitgo.store.R
import com.outfitgo.store.core.util.convertISODateToReadableDate

@Composable
fun OrderSummerySection(
    orderNumber: Int,
    orderDate: String,
    itemsCount: Int,
    paymentStatus: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),

    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.orders_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                Column {
                    Text(
                        text = stringResource(R.string.order_summery),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = convertISODateToReadableDate(orderDate),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF6E5849),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SummeryItem(
                    label = stringResource(R.string.order_number),
                    value = "#$orderNumber"
                )

                SummeryItem(
                    label = stringResource(R.string.items),
                    value = stringResource(R.string.items_count, itemsCount)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.payment_status),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                    )

                    Text(
                        modifier = Modifier
                            .background(
                                color = Color(0x403CFF00),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(vertical = 4.dp, horizontal = 16.dp),
                        text = paymentStatus,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                    )
                }
            }
        }

    }
}

@Composable
fun SummeryItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
        )

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
        )
    }
}