package com.outfitgo.store.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.usecase.auth.LoginWithEmailAndPasswordUseCase
import com.outfitgo.store.domain.usecase.auth.ValidateEmailUseCase
import com.outfitgo.store.domain.usecase.auth.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG = "LoginViewModel"
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase
): ViewModel() {

    private val _state = MutableStateFlow(LoginScreenUiState())
    val state = _state.asStateFlow()

    private val _effect: MutableSharedFlow<LoginScreenEffect> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: LoginScreenIntent) {
        when (intent) {
            is LoginScreenIntent.EmailChanged -> {
                _state.update { it.copy(email = intent.newEmail) }
                Log.i(TAG, "processIntent: updated email")
            }
            is LoginScreenIntent.PasswordChanged -> {
                _state.update { it.copy(password = intent.newPassword) }
                Log.i(TAG, "processIntent: updated password")
            }
            LoginScreenIntent.LoginClicked -> login()
        }
    }

    private fun login() {
        viewModelScope.launch {
            // validate input
            val emailValidation = validateEmailUseCase(_state.value.email)
            val passwordValidation = validatePasswordUseCase(_state.value.password)

            Log.i(TAG, "login: validated email and password")
            if(emailValidation.isValid && passwordValidation.isValid) {
                _state.update { it.copy(isLoading = true) }
                withContext(Dispatchers.IO) {
                    val user = loginWithEmailAndPasswordUseCase(_state.value.email, _state.value.password)
                    if(user != null) {
                        _effect.emit(LoginScreenEffect.DisplaySnack("Login Success: ${user.displayName}"))
                        _state.update { it.copy(isLoading = false) }
                        _effect.emit(LoginScreenEffect.GoToHomeScreen)
                    }
                }

            } else {
                _state.update { it.copy(
                    emailErrorMsg = emailValidation.error ?: "",
                    passwordErrorMsg = passwordValidation.error ?: "",
                    isLoading = false
                ) }
                _effect.emit(LoginScreenEffect.DisplaySnack("ERROR HAppend"))
            }
        }


    }
}

