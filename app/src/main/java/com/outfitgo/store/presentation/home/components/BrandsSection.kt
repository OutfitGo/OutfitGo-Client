package com.outfitgo.store.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.presentation.components.shimmerBrush

@Composable
fun BrandsSection(
    brands: List<Collection>,
    isLoading: Boolean,
    onBrandClicked: (Collection) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.brands),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(items = brands) { index, brand ->
                BrandItem(
                    collection = brand,
                    onBrandClicked = onBrandClicked
                )
            }

            if (isLoading) {
                item {
                    BrandsPageLoadingState()
                }
            }
        }
    }
}

@Composable
private fun BrandItem(
    collection: Collection,
    onBrandClicked: (Collection) -> Unit
) {
    Box(
        modifier = Modifier
            .size(104.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    onBrandClicked(collection)
                }
            ),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
            model = collection.imageUrl,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
    }
}

@Composable
fun BrandsPageLoadingState() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(104.dp)
                .background(brush = shimmerBrush(), shape = RoundedCornerShape(16.dp))
        )

        Box(
            modifier = Modifier
                .size(104.dp)
                .background(brush = shimmerBrush(), shape = RoundedCornerShape(16.dp))
        )

        Box(
            modifier = Modifier
                .size(104.dp)
                .background(brush = shimmerBrush(), shape = RoundedCornerShape(16.dp))
        )
    }
}