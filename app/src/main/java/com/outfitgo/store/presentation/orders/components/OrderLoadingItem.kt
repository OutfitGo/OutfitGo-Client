package com.outfitgo.store.presentation.orders.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.outfitgo.store.presentation.components.shimmerBrush
import com.outfitgo.store.presentation.ui.theme.DottedShape
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme

@Composable
fun OrderLoadingItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(8.dp)
    ){
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(0.3f)
                .height(10.dp)
                .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
        )

        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(0.7f)
                .height(10.dp)
                .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
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
        ){
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.4f)
                    .height(10.dp)
                    .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
            )

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.3f)
                    .height(10.dp)
                    .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.35f)
                    .height(10.dp)
                    .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
            )

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.35f)
                    .height(10.dp)
                    .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.3f)
                    .height(10.dp)
                    .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
            )

            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.45f)
                    .height(10.dp)
                    .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            repeat(3) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(brush = shimmerBrush(), shape = RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderLoadingItemPreview() {
    OutfitGoTheme {
        OrderLoadingItem()
    }
}