package com.outfitgo.store.domain.usecase.address

import com.outfitgo.store.domain.repository.address.AddressRepository
import javax.inject.Inject

class GetUserAddressesUseCase @Inject constructor(private val addressRepository: AddressRepository) {
    suspend operator fun invoke(token: String) = addressRepository.getAddresses(token)
}