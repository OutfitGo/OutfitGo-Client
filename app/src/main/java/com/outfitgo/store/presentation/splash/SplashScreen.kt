package com.outfitgo.store.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun OutfitGoSplashScreen(
    viewModel: SplashViewModel,
    onGoToHome: () -> Unit,
    onGoToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(Unit) {
        viewModel.effect.collect { event ->
            when(event) {
                SplashEffect.GoToHomeScreen -> onGoToHome()
                SplashEffect.GoToLoginScreen -> onGoToLogin()
            }
        }
    }

    Box(modifier, contentAlignment = Alignment.Center) {
        Text("Splash Screen", style = MaterialTheme.typography.displayMedium)
    }
}