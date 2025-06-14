package com.outfitgo.store.presentation.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.outfitgo.store.R
import com.outfitgo.store.presentation.register.RegisterIntent
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private const val TAG = "LoginScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginScreenUiState,
    onIntent: (LoginScreenIntent) -> Unit,
    effectFlow: Flow<LoginScreenEffect>,
    onGoToHome: () -> Unit,
    onGoToSignup: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPassword by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is LoginScreenEffect.DisplaySnack -> {
                    snackbarHostState.showSnackbar(effect.msg)
                }

                LoginScreenEffect.GoToHomeScreen -> {
                    onGoToHome()
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                actions = {
                    OutlinedButton(onClick = { onIntent(LoginScreenIntent.LoginAsGuestClicked) }) {
                        Text(stringResource(R.string.continue_as_guest))
                        Icon(
                            Icons.AutoMirrored.Outlined.Login,
                            contentDescription = stringResource(R.string.continue_as_guest)
                        )
                    }
                },
                title = { Text(stringResource(R.string.app_name), fontWeight = FontWeight.ExtraBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {

            Text(stringResource(R.string.login), style = MaterialTheme.typography.headlineLarge)
            Text(stringResource(R.string.hi_welcome_back_you_have_been_missed), modifier = Modifier.alpha(0.7f))


            OutlinedTextField(
                value = state.email,
                onValueChange = { onIntent(LoginScreenIntent.EmailChanged(it)) },
                label = { Text(stringResource(R.string.email)) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = stringResource(R.string.email)) },
                singleLine = true,
                supportingText = {
                    if(state.emailErrorMsg.isNotBlank()) {
                        Text(
                            state.emailErrorMsg,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { onIntent(LoginScreenIntent.PasswordChanged(it)) },
                singleLine = true,
                label = { Text(stringResource(R.string.password)) },
                modifier = Modifier
                    .fillMaxWidth(),
                supportingText = {
                    if (state.passwordErrorMsg.isNotBlank()) {
                        Text(
                            state.passwordErrorMsg,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = stringResource(R.string.password)) },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = "toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
            )

            Button(
                onClick = { onIntent(LoginScreenIntent.LoginClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) CircularProgressIndicator() else Text(stringResource(R.string.login))
            }

            Row {
                Text(stringResource(R.string.don_t_have_an_account))
                Text(
                    stringResource(R.string.sign_in), color = Color.Blue,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable(onClick = onGoToSignup)
                )
            }
        }

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    OutfitGoTheme {
        LoginScreen(
            state = LoginScreenUiState(),
            onIntent = { },
            effectFlow = flow { LoginScreenEffect.DisplaySnack("") },
            modifier = Modifier.fillMaxSize(),
            onGoToHome = {},
            onGoToSignup = {}
        )
    }


}