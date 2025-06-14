package com.outfitgo.store.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun UnAuthenticatedScreen(modifier: Modifier = Modifier, onClickLogin: () -> Unit) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Outlined.Lock, contentDescription = "UnAuthorized Access",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(8.dp))
        Text("UnAuthorized Access!", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(8.dp))
        Text(
            "you can't access this feature in guest mode, please login to your account to use it or create a new account",
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))
        Button(
            onClick = onClickLogin
        ) { Text("Login to your account") }

    }
}