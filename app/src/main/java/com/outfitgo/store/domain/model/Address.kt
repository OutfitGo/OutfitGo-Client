package com.outfitgo.store.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val id:String,
    val firstName:String,
    val lastName:String,
    val line:String,
    val city:String,
    val isDefault:Boolean
)
