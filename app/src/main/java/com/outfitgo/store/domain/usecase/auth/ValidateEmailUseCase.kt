package com.outfitgo.store.domain.usecase.auth

import javax.inject.Inject


class ValidateEmailUseCase @Inject constructor() {
    operator fun invoke(email: String): ValidationResult {
        return if(email.isEmpty()) ValidationResult(isValid = false, error = "Email is Empty")
        else ValidationResult(isValid = true)
    }
}