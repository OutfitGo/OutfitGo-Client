package com.outfitgo.store.domain.usecase.auth


import javax.inject.Inject


class ValidateEmailUseCase @Inject constructor() {

    private val EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"

    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(isValid = false, error = "Email cannot be empty.")
        }

        if (!email.matches(EMAIL_REGEX.toRegex())) {
            return ValidationResult(
                isValid = false, error = "Invalid email format. Please enter a valid email address."
            )
        }

        return ValidationResult(isValid = true)
    }
}