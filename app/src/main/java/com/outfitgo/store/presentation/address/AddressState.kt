package com.outfitgo.store.presentation.address

import com.outfitgo.store.domain.model.Address

data class AddressState(
    val addresses: List<Address> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val cities: List<String> = emptyList()
)