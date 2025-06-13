package com.outfitgo.store.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.outfitgo.store.R
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme


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

    SplashScreenContent(
        modifier = modifier
    )

}


@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = stringResource(R.string.app_name),
                Modifier
                    .size(200.dp)
                    .clip(
                        CircleShape
                    )
            )
            Text(stringResource(R.string.app_name), style = MaterialTheme.typography.displaySmall)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun SplashScreenPreview() {
    OutfitGoTheme {
        SplashScreenContent(
            modifier = Modifier.fillMaxSize()
        )
    }
}


