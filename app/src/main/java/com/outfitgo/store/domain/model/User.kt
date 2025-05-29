package com.outfitgo.store.domain.model

data class User(
    val id: String = "ID",
    val firstname: String = "FIRST",
    val lastname: String = "LAST",
    val displayName: String = "DISPLAY",
    val email: String = "EMAIL",
    val password: String = "PASSWORD"
)
