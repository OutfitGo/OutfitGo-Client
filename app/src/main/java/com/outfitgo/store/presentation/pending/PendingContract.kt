package com.outfitgo.store.presentation.pending

data class PendingState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
)

sealed interface PendingIntent {
    data object RegisterUser: PendingIntent

    data object ReSendVerificationEmail: PendingIntent
}

sealed interface PendingEffect {
    data class SendSnackbar(val msg: String): PendingEffect
    object GoToHome: PendingEffect
}