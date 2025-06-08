package com.outfitgo.store.presentation.register

data class RegisterUiState(
    val firstName: String = "",
    val firstNameErrorMsg: String = "",
    val lastName: String = "",
    val lastNameErrorMsg: String = "",
    val email: String = "",
    val emailErrorMsg: String = "",
    val password: String = "",
    val passwordErrorMsg: String = "",
    val isLoading: Boolean = false
)

sealed interface RegisterIntent {
    data class FirstNameChanged(val firstName: String): RegisterIntent
    data class LastNameChanged(val lastName: String): RegisterIntent
    data class EmailChanged(val email: String): RegisterIntent
    data class PasswordChanged(val password: String): RegisterIntent
    data object Register: RegisterIntent

    // navigation related intents
    data object ContinueAsGuest: RegisterIntent
    data object GoToLogin: RegisterIntent
}

sealed interface RegisterEffect {
    data class SendSnackBar(val msg: String): RegisterEffect
    object GoToHome: RegisterEffect
}
