package com.outfitgo.store.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.usecase.auth.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor (
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase
): ViewModel() {
    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            if(isUserLoggedInUseCase.execute()) {
                _effect.emit(SplashEffect.GoToHomeScreen)
            } else {
                _effect.emit(SplashEffect.GoToLoginScreen)
            }
        }
    }
}

