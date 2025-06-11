package com.outfitgo.store.presentation.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel(),
    onContinueAsGuestClicked: () -> Unit,
    onGoToLoginClicked: () -> Unit,
    onGoToHome: () -> Unit,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    RegisterScreenContent(
        state = state.value,
        onEvent = { event ->
            when (event) {
                RegisterIntent.ContinueAsGuest -> onContinueAsGuestClicked()
                RegisterIntent.GoToLogin -> onGoToLoginClicked()
                else -> viewModel.processIntent(event)
            }
        },
        effectFlow = viewModel.effect,
        modifier = modifier,
        onGoToHome = onGoToHome
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreenContent(
    state: RegisterUiState,
    onEvent: (RegisterIntent) -> Unit,
    effectFlow: SharedFlow<RegisterEffect>,
    onGoToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPassword by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        effectFlow.collect {effect ->
            when(effect) {
                is RegisterEffect.SendSnackBar -> {
                    snackbarHostState.showSnackbar(effect.msg)
                }
                is RegisterEffect.GoToHome -> onGoToHome()
                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("OutfitGo", fontWeight = FontWeight.Bold) },
                actions = {
                    OutlinedButton(onClick = { onEvent(RegisterIntent.ContinueAsGuest) }) {
                        Text("Continue As Guest")
                        Icon(
                            Icons.AutoMirrored.Outlined.Login,
                            contentDescription = "Continue As Guest"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // header
            Text("Create Account", style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(8.dp))
            Text(
                "Fill Information below to Register as a New User",
                modifier = Modifier
                    .alpha(0.7f)
                    .fillMaxWidth(0.7f),
                textAlign = TextAlign.Center
            )
            // -------------------------

            // fields
            OutlinedTextField(
                value = state.firstName,
                onValueChange = { onEvent(RegisterIntent.FirstNameChanged(it)) },
                singleLine = true,
                label = { Text("First Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                supportingText = {
                    Text(
                        state.firstNameErrorMsg,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "first name") }
            )

            OutlinedTextField(
                value = state.lastName,
                onValueChange = { onEvent(RegisterIntent.LastNameChanged(it)) },
                singleLine = true,
                label = { Text("Last Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                supportingText = {
                    Text(
                        state.lastNameErrorMsg,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = "last name") }
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = { onEvent(RegisterIntent.EmailChanged(it)) },
                singleLine = true,
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                supportingText = {
                    Text(
                        state.emailErrorMsg,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                leadingIcon = { Icon(Icons.Outlined.Mail, contentDescription = "email") }
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = { onEvent(RegisterIntent.PasswordChanged(it)) },
                singleLine = true,
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                supportingText = {
                    Text(
                        state.passwordErrorMsg,
                        color = MaterialTheme.colorScheme.error
                    )
                },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = "email") },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = "toggle password visibility"
                        )
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
            )

            Button(
                onClick = { onEvent(RegisterIntent.Register) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Register Now")
                }
            }

            Row {
                Text("Already Have an Account? ")
                Text(
                    "Log in", color = Color.Blue,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable(onClick = { onEvent(RegisterIntent.GoToLogin) })
                )
            }

        }

    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RegisterScreenPreview() {
    OutfitGoTheme {
        RegisterScreenContent(
            state = RegisterUiState(
                firstName = "boody",
                firstNameErrorMsg = "boody error",
                lastName = "ahmed",
                lastNameErrorMsg = "ahmed error",
                email = "boody@gamil.com",
                emailErrorMsg = "email error",
                password = "Password",
                passwordErrorMsg = "password error",
                isLoading = false
            ),
            onEvent = { },
            effectFlow = MutableSharedFlow(),
            modifier = Modifier.fillMaxSize(),
            onGoToHome = {}
        )
    }

}