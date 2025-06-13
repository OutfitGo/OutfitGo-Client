package com.outfitgo.store.presentation.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun ProfileItem(icon: ImageVector, title: String, onClick: () -> Unit, modifier: Modifier) {
    Column(modifier = modifier.clickable { onClick() }) {
        ListItem(
            headlineContent = { Text(title) },
            modifier = Modifier.fillMaxSize(),
            leadingContent = {
                Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
            },
            trailingContent = {
                Icon(
                    Icons.AutoMirrored.Outlined.NavigateNext,
                    contentDescription = "Go To $title"
                )
            },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
        )
        HorizontalDivider()
    }

}
