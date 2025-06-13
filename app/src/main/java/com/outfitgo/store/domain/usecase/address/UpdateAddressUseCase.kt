package com.outfitgo.store.domain.usecase.address

import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.domain.repository.address.AddressRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(token: String, address: Address) =
        addressRepository.updateAddress(token, address)
}