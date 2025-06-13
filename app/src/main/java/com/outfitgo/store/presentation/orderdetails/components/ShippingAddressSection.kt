package com.outfitgo.store.presentation.orderdetails.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.order.OrderShippingAddress

@Composable
fun ShippingAddressSection(
    shippingAddress: OrderShippingAddress
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location),
                    contentDescription = null,
                    tint = Color(0xFF5D57FA)
                )

                Text(
                    text = stringResource(R.string.shipping_address),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = shippingAddress.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = shippingAddress.addressLine1,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = shippingAddress.addressLine2,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = shippingAddress.city,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}