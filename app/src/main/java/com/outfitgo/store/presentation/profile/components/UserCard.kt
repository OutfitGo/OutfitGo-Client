package com.outfitgo.store.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.outfitgo.store.domain.model.User

@Composable
fun UserCard(user: User, modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // image
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary,
                        )
                    )
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                "${user.firstname.firstOrNull()}${user.lastname.firstOrNull()}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        // name
        Text(user.displayName, style = MaterialTheme.typography.titleLarge)
        // email address
        Text(
            user.email,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.alpha(0.7f)
        )
    }
}