package com.outfitgo.store.presentation.splash

sealed interface SplashEffect {
    object GoToHomeScreen: SplashEffect
    object GoToLoginScreen: SplashEffect
}