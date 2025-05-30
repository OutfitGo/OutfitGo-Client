package com.outfitgo.store.domain.usecase.auth

import javax.inject.Inject

class ValidatePasswordUseCase @Inject constructor() {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 5
    }

    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(isValid = false, error = "Password can't be empty.")
        }

        if (password.length < MIN_PASSWORD_LENGTH) { // Changed minimum length to 8 for better security
            return ValidationResult(isValid = false, error = "Password is too short. It must be at least $MIN_PASSWORD_LENGTH characters long.")
        }

        if (!password.contains(Regex("[A-Z]"))) {
            return ValidationResult(isValid = false, error = "Password must contain at least one uppercase letter.")
        }

        if (!password.contains(Regex("[a-z]"))) {
            return ValidationResult(isValid = false, error = "Password must contain at least one lowercase letter.")
        }

        if (!password.contains(Regex("[0-9]"))) {
            return ValidationResult(isValid = false, error = "Password must contain at least one digit.")
        }

        return ValidationResult(isValid = true)
    }
}