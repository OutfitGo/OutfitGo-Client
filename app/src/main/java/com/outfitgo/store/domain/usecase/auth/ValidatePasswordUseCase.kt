package com.outfitgo.store.domain.usecase.auth

import javax.inject.Inject


class ValidatePasswordUseCase @Inject constructor() {
    operator fun invoke(password: String): ValidationResult {
        if(password.length < 5)
            return ValidationResult(isValid = false, error = "password is too short")

        if(password.isBlank())
            return ValidationResult(isValid = false, error = "password can't be empty")

        return ValidationResult(isValid = true)
    }
}