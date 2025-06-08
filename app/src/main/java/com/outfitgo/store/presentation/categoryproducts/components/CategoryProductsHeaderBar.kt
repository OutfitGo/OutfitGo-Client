package com.outfitgo.store.presentation.categoryproducts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme

@Composable
fun CategoryProductsHeaderBar(
    category: String,
    onBackClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ){
        IconButton(
            modifier = Modifier
                .size(50.dp)
                .background(color = Color(0xFFF1F1F1), shape = CircleShape)
                .align(Alignment.CenterStart),
            onClick = onBackClicked
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = category,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeHeaderBarPreview() {
    OutfitGoTheme {
        CategoryProductsHeaderBar(
            category = "Men Collection",
            onBackClicked = {}
        )
    }
}