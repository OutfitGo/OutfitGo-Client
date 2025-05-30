package com.outfitgo.store.presentation.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier
) {
    var showPassword by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is LoginScreenEffect.DisplaySnack -> {
                    Log.d(TAG, "LoginScreen: ${effect.msg}")
                    snackbarHostState.showSnackbar(effect.msg)
                }
                LoginScreenEffect.GoToHomeScreen -> {
                    // navigation
                    Log.i(TAG, "LoginScreen: Navigating")
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
                        Text("Login As Guest")
                        Icon(Icons.AutoMirrored.Outlined.Login, contentDescription = "Login As Guest")
                    }
                },
                title = { Text("OutfitGo", fontWeight = FontWeight.ExtraBold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {

            Text("Login", style = MaterialTheme.typography.headlineLarge)
            Text("Hi! Welcome back, you have been missed", modifier = Modifier.alpha(0.7f))


            OutlinedTextField(
                value = state.email,
                onValueChange = { onIntent(LoginScreenIntent.EmailChanged(it)) },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                singleLine = true,
                supportingText = {Text(state.emailErrorMsg)},
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { onIntent(LoginScreenIntent.PasswordChanged(it)) },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                singleLine = true,
                trailingIcon = {
                    if(showPassword) Icon(Icons.Default.Settings, contentDescription = null)
                    else Icon(Icons.Outlined.AccountCircle, contentDescription = null)
                },
                supportingText = {Text(state.passwordErrorMsg)},
                visualTransformation = if(showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Checkbox(checked = showPassword, onCheckedChange = {showPassword = it})
                Text("Show Password")
            }

            Button(
                onClick = { onIntent(LoginScreenIntent.LoginClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if(state.isLoading) CircularProgressIndicator() else Text("Login")
            }

            Row {
                Text("Don't Have an Account? ")
                Text("Sign in", color = Color.Blue,
                    textDecoration = TextDecoration.Underline)
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
            onIntent = {  },
            effectFlow = flow { LoginScreenEffect.DisplaySnack("") },
            modifier = Modifier.fillMaxSize()
        )
    }


}