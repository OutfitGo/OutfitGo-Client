package com.outfitgo.store.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
