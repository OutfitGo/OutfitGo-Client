package com.outfitgo.store.presentation.login

// state
data class LoginScreenUiState(
    val email: String = "",
    val emailErrorMsg: String = "",
    val password: String = "",
    val passwordErrorMsg: String = "",
    val isLoading: Boolean = false
)

// intents (actions from user)
sealed interface LoginScreenIntent {
    data class EmailChanged(val newEmail: String): LoginScreenIntent
    data class PasswordChanged(val newPassword: String): LoginScreenIntent
    object LoginClicked: LoginScreenIntent
}

// effects (actions from vm to ui)
sealed interface LoginScreenEffect {
    object GoToHomeScreen: LoginScreenEffect
    data class DisplaySnack(val msg: String): LoginScreenEffect
}