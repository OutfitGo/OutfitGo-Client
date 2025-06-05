package com.outfitgo.store.presentation.util.auth

import android.util.Patterns
import com.outfitgo.store.domain.usecase.auth.ValidationResult

private const val MIN_PASSWORD_LENGTH: Int = 5

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
    if (this.length < MIN_PASSWORD_LENGTH) {
        return ValidationResult(false, "Password must be at least $MIN_PASSWORD_LENGTH characters long.")
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