package com.outfitgo.store.presentation.address

import com.outfitgo.store.domain.model.Address

sealed interface AddressIntent {
    object getAdrresses : AddressIntent
    data class AddNewAddress(val firstName:String,val lastName:String,val line:String,val city:String):AddressIntent
    data class requestDeleteAddress(val id: String):AddressIntent
    data class deleteAddress(val id:String): AddressIntent
    data class updateAddress(val address: Address):AddressIntent
    data class updateDefaultAddress(val addressId:String):AddressIntent
    object getCities:AddressIntent
}