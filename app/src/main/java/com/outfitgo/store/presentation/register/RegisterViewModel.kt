package com.outfitgo.store.presentation.register

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.usecase.auth.RegisterNewUserUseCase
import com.outfitgo.store.domain.usecase.auth.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RegisterViewModel"

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerNewUserUseCase: RegisterNewUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: RegisterIntent) {
        when(intent) {
            is RegisterIntent.EmailChanged -> {
                _state.update { it.copy(email = intent.email) }
            }
            is RegisterIntent.FirstNameChanged -> {
                _state.update { it.copy(firstName = intent.firstName) }
            }
            is RegisterIntent.LastNameChanged -> {
                _state.update { it.copy(lastName = intent.lastName) }
            }
            is RegisterIntent.PasswordChanged -> {
                _state.update { it.copy(password = intent.password) }
            }
            RegisterIntent.Register -> register()
            else -> Unit
        }
    }

    private fun register() {
        viewModelScope.launch {
            val isValidInput: Boolean = validateInput()
            if(isValidInput) {
                _state.update { it.copy(isLoading = true) }
                try {
                    val user = registerNewUserUseCase.execute(
                        firstName = _state.value.firstName,
                        lastName = _state.value.lastName,
                        email = _state.value.email,
                        password = _state.value.password
                    )
                    if(user != null) { // success
                        _effect.emit(RegisterEffect.GoToHome)
                        Log.i(TAG, "register: successfully added user ${user.firstname}")
                        Log.i(TAG, "register: id: ${user.id}")
                    } else { // failure
                        _state.update { it.copy(isLoading = false) }
                        _effect.emit(RegisterEffect.SendSnackBar("Failure"))
                    }
                } catch (exp: Exception) {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(RegisterEffect.SendSnackBar(exp.message ?: "ERROR"))
                }
            }
        }
    }

    private fun validateInput(): Boolean {
        val results: MutableList<ValidationResult> = mutableListOf()

        val firstNameResult = _state.value.firstName.isValidName()
        _state.update { it.copy(firstNameErrorMsg = firstNameResult.error ?: "") }
        results.add(firstNameResult)

        val lastNameResult = _state.value.lastName.isValidName()
        _state.update { it.copy(lastNameErrorMsg = lastNameResult.error ?: "") }
        results.add(lastNameResult)

        val emailResult = _state.value.email.isValidEmail()
        _state.update { it.copy(emailErrorMsg = emailResult.error ?: "") }
        results.add(emailResult)

        val passwordResult = _state.value.password.isValidPassword()
        _state.update { it.copy(passwordErrorMsg = passwordResult.error ?: "") }
        results.add(passwordResult)

        return results.all { it.isValid }
    }


}

fun String.isValidName(): ValidationResult {
    if (this.isBlank()) {
        return ValidationResult(false, "Name cannot be empty or blank.")
    }
    return ValidationResult(isValid = true)
}
fun String.isValidEmail(): ValidationResult {
    if (this.isBlank()) {
        return ValidationResult(false, "Email cannot be empty or blank.")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(this).matches()) {
        return ValidationResult(false, "Please enter a valid email address format.")
    }
    return ValidationResult(true)
}
fun String.isValidPassword(): ValidationResult {
    if (this.isBlank()) {
        return ValidationResult(false, "Password cannot be empty or blank.")
    }

    // Minimum length check
    if (this.length < 5) {
        return ValidationResult(false, "Password must be at least 8 characters long.")
    }

    /*// Regex for at least one uppercase letter
    if (!this.matches(Regex(".*[A-Z].*"))) {
        return ValidationResult(false, "Password must contain at least one uppercase letter.")
    }

    // Regex for at least one lowercase letter
    if (!this.matches(Regex(".*[a-z].*"))) {
        return ValidationResult(false, "Password must contain at least one lowercase letter.")
    }

    // Regex for at least one digit
    if (!this.matches(Regex(".*\\d.*"))) {
        return ValidationResult(false, "Password must contain at least one digit.")
    }

    // Regex for at least one special character (you can customize this set)
    // Common special characters: !@#$%^&*()-_=+\|[{]};:'",<.>/?`~
    if (!this.matches(Regex(".*[!@#\$%^&*()\\-_=+\\|\\[{\\]};:'\",<.>/?`~].*"))) {
        return ValidationResult(false, "Password must contain at least one special character.")
    }
*/

    // If all checks pass, the password is considered valid
    return ValidationResult(true)
}