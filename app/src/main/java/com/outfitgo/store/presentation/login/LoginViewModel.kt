package com.outfitgo.store.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.usecase.auth.LoginWithEmailAndPasswordUseCase
import com.outfitgo.store.domain.usecase.auth.ValidationResult
import com.outfitgo.store.presentation.util.auth.isValidEmail
import com.outfitgo.store.presentation.util.auth.isValidPassword
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
            val isInputValid = validateInput()
            if(isInputValid) {
                try {
                    _state.update { it.copy(isLoading = true) }
                    val user =
                        loginWithEmailAndPasswordUseCase(_state.value.email, _state.value.password)
                    user?.let {
                        _effect.emit(LoginScreenEffect.DisplaySnack("Login Success: ${it.displayName}"))
                        _state.update { it.copy(isLoading = false) }
                        _effect.emit(LoginScreenEffect.GoToHomeScreen)
                    }
                } catch (exp: Exception) {
                    _effect.emit(LoginScreenEffect.DisplaySnack(exp.localizedMessage ?: "ERROR"))
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }


    private fun validateInput(): Boolean {
        val results: MutableList<ValidationResult> = mutableListOf()

        val emailResult = _state.value.email.isValidEmail()
        _state.update { it.copy(emailErrorMsg = emailResult.error ?: "") }
        results.add(emailResult)

        val passwordResult = _state.value.password.isValidPassword()
        _state.update { it.copy(passwordErrorMsg = passwordResult.error ?: "") }
        results.add(passwordResult)

        return results.all { it.isValid }
    }
}

