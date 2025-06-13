package com.outfitgo.store.presentation.settings.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.outfitgo.store.R

private const val TAG = "SettingsScreen"

@Composable
fun SettingsScreen(
    onNavToCurrencySettings: () -> Unit,
    onNavToWishlistScreen: () -> Unit,
    onNavToAddressScreen: () -> Unit,
    onNavToOrdersScreen: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = 42.dp, bottom = 24.dp)
            .padding(horizontal = 24.dp)
    ) {
        Text(stringResource(R.string.settings), style = MaterialTheme.typography.titleLarge)
        SettingsScreenContent(
            onNavToCurrencySettings = onNavToCurrencySettings,
            onNavToWishlistScreen = onNavToWishlistScreen,

            onNavToAddressScreen = onNavToAddressScreen,
            onNavToOrdersScreen = onNavToOrdersScreen,
            onLogoutClicked = {
                Log.i(TAG, "SettingsScreen: logout clicked")
            }
        )
    }
}

@Composable
fun SettingsScreenContent(
    onNavToCurrencySettings: () -> Unit,
    onNavToWishlistScreen: () -> Unit,
    onNavToAddressScreen: () -> Unit,
    onNavToOrdersScreen: () -> Unit,
    onLogoutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        SettingRow(
            title = stringResource(R.string.currency),
            icon = R.drawable.ic_currency,
            onClick = onNavToCurrencySettings
        )

        SettingRow(
            title = "Wishlist",
            icon = R.drawable.wishlist_icon,
            onClick = onNavToWishlistScreen
        )

        SettingRow(
            title = "Address",
            icon = R.drawable.ic_address,
            onClick = onNavToAddressScreen
          )

        SettingRow(
            title = "Orders",
            icon = R.drawable.orders_icon,
            onClick = onNavToOrdersScreen
        )
        
        SettingRow(
            title = "Logout",
            icon = R.drawable.ic_launcher_foreground,
            onClick = {
                onLogoutClicked()
            }
        )
    }
}


@Composable
fun SettingRow(title: String, icon: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clickable { onClick() }
                .padding(horizontal = 16.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp)
            )
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.ic_next),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(24.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    }
}