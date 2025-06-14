package com.outfitgo.store.presentation.address

sealed interface AddressEffect {
    object onAddressRemoved:AddressEffect
    data class onAddressRemove(val id:String):AddressEffect
    object onAddressUpdated:AddressEffect
    data class onAddressUpdateError(val message:String):AddressEffect
    object onAddAddress:AddressEffect
}