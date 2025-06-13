package com.outfitgo.store.domain.usecase.address

import com.outfitgo.store.domain.repository.address.AddressRepository
import javax.inject.Inject

class GetDefaultAddressUseCase @Inject constructor(private val repository: AddressRepository) {
    suspend operator fun invoke(token: String) = repository.getDefaultAddress(token)
}