package com.outfitgo.store.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme

@Composable
fun NoNetworkScreen(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                )
            )
        )
    ) {
        Icon(
            Icons.Outlined.WifiOff,
            contentDescription = "No network",
            modifier = Modifier.size(200.dp),
            tint = Color.Black.copy(alpha = 0.7f)
        )
        Text("No Internet!!", style = MaterialTheme.typography.displaySmall, )
        Text("please check your network to continue using OutfitGo", Modifier.fillMaxWidth().padding(16.dp), textAlign = TextAlign.Center)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NoNetworkScreenPreview() {
    OutfitGoTheme {
        NoNetworkScreen(Modifier.fillMaxSize())
    }
}