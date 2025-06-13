package com.outfitgo.store.presentation.profile

import com.outfitgo.store.domain.model.User

data class ProfileState(
    val user: User = User(
        id = "ID", firstname = "FName",
        lastname = "Lname",
        displayName = "FirstName LastName",
        email = "fl@gmail.com",
    ),
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = true,
)

sealed interface ProfileIntent {
    object LoadProfile: ProfileIntent
    object Logout: ProfileIntent
    object GoToOrders: ProfileIntent
    object GoToAboutUs: ProfileIntent
    object GoToAddresses: ProfileIntent
    object GoToSettings: ProfileIntent
    object GoToWishlist: ProfileIntent
    object GoToLogin: ProfileIntent
}

sealed interface ProfileEffect {
    object LogoutSuccess : ProfileEffect
    data class SendSnackBar(val msg: String): ProfileEffect
}