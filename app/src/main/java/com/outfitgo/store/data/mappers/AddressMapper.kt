package com.outfitgo.store.data.mappers

import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.storefront.GetAddressQuery

fun GetAddressQuery.Addresses.toDomain(): List<Address> {
    return edges.map { it.node.toDomain() }
}

fun GetAddressQuery.Node.toDomain(): Address {
    return Address(
        id = id,
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        line = address1 ?: "",
        city = city ?: "",
        isDefault = false
    )
}

fun GetAddressQuery.DefaultAddress.toDomain(): Address {
    return Address(
        id = id,
        firstName = firstName ?: "",
        lastName = lastName ?: "",
        line = address1 ?: "",
        city = city ?: "",
        isDefault = true
    )
}
