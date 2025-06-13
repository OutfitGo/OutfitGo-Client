package com.outfitgo.store.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.Const
import com.outfitgo.store.domain.usecase.auth.GetSavedUserIdUseCase
import com.outfitgo.store.domain.usecase.auth.GetSavedUserTokenUseCase
import com.outfitgo.store.domain.usecase.auth.IsUserLoggedInUseCase
import com.outfitgo.store.domain.usecase.auth.SaveUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SplashViewModel"
@HiltViewModel
class SplashViewModel @Inject constructor (
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    private val getSavedUserTokenUseCase: GetSavedUserTokenUseCase,
    private val getSaveUserIdUseCase: GetSavedUserIdUseCase
): ViewModel() {
    private val _effect = MutableSharedFlow<SplashEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            Log.i(TAG, "token: ${getSavedUserTokenUseCase.execute()}")
            Log.i(TAG, "id : ${getSaveUserIdUseCase.execute()}")
            if(isUserLoggedInUseCase.execute()) {
                _effect.emit(SplashEffect.GoToHomeScreen)
                Const.isLoggedIn=true
            } else {
                _effect.emit(SplashEffect.GoToLoginScreen)
            }
        }
    }
}

