package com.outfitgo.store.domain.usecase.auth

data class ValidationResult(val isValid: Boolean, val error: String? = null)