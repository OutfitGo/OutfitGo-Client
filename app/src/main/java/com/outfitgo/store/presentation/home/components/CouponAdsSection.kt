package com.outfitgo.store.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CouponAdsSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth()
                .height(175.dp)
                .background(
                    color = Color(0xFFA2A2A2),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        Box(
            Modifier
                .width(75.dp)
                .height(10.dp)
                .background(
                    color = Color(0xFFA2A2A2),
                    shape = RoundedCornerShape(16.dp)
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CouponAdsSectionPreview() {
    CouponAdsSection()
}