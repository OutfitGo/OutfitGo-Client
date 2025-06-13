package com.outfitgo.store.presentation.pending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun PendingScreen(
    email: String, password: String, firstName: String, lastName: String,
    onGoToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PendingViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setDate(email, password, firstName, lastName)
    }

    PendingScreenContent(
        state = state.value,
        onEvent = viewModel::processIntent,
        effect = viewModel.effect,
        modifier = modifier,
        onGoToHome = onGoToHome
    )

}

@Composable
fun PendingScreenContent(
    state: PendingState,
    onEvent: (PendingIntent) -> Unit,
    effect: SharedFlow<PendingEffect>,
    onGoToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        effect.collect { effect ->
            when (effect) {
                PendingEffect.GoToHome -> onGoToHome()
                is PendingEffect.SendSnackbar -> {
                    snackbarHostState.showSnackbar(effect.msg)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "We Have Sent a Verification email to ${state.email}. \n Click the Button after clicking on link sent to your email",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { onEvent(PendingIntent.RegisterUser) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("I Have Verified my Email")
                }
            }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { onEvent(PendingIntent.ReSendVerificationEmail) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Resend Verification Email")
                }
            }

        }
    }


}