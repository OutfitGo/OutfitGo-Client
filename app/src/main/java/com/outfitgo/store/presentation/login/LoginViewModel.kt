package com.outfitgo.store.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.Const
import com.outfitgo.store.domain.usecase.auth.LoginWithEmailAndPasswordUseCase
import com.outfitgo.store.domain.usecase.auth.ValidateEmailUseCase
import com.outfitgo.store.domain.usecase.auth.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginScreenUiState())
    val state = _state.asStateFlow()

    private val _effect: MutableSharedFlow<LoginScreenEffect> = MutableSharedFlow()
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: LoginScreenIntent) {
        when (intent) {
            is LoginScreenIntent.EmailChanged -> {
                _state.update { it.copy(email = intent.newEmail) }
            }

            is LoginScreenIntent.PasswordChanged -> {
                _state.update { it.copy(password = intent.newPassword) }
            }

            LoginScreenIntent.LoginClicked -> login()
            LoginScreenIntent.LoginAsGuestClicked -> viewModelScope.launch {
                _effect.emit(
                    LoginScreenEffect.GoToHomeScreen
                )
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            val isInputValid = isInputValid()
            if(isInputValid) {
                try {
                    _state.update { it.copy(isLoading = true) }
                    val user =
                        loginWithEmailAndPasswordUseCase(_state.value.email, _state.value.password)
                    user?.let {
                        _effect.emit(LoginScreenEffect.DisplaySnack("Login Success: ${it.displayName}"))
                        _state.update { it.copy(isLoading = false) }
                        Const.isLoggedIn=true
                        _effect.emit(LoginScreenEffect.GoToHomeScreen)
                    }
                } catch (exp: Exception) {
                    _effect.emit(LoginScreenEffect.DisplaySnack(exp.localizedMessage ?: "ERROR"))
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun isInputValid(): Boolean {
        val emailValidation = validateEmailUseCase(_state.value.email)
        val passwordValidation = validatePasswordUseCase(_state.value.password)
        if (emailValidation.isValid && passwordValidation.isValid) {
            return true
        } else {
            _state.update {
                it.copy(
                    emailErrorMsg = emailValidation.error ?: "",
                    passwordErrorMsg = passwordValidation.error ?: "",
                    isLoading = false
                )
            }
            return false
        }
    }
}

