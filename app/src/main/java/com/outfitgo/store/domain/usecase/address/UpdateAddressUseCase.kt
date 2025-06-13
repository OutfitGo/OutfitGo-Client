package com.outfitgo.store.domain.usecase.address

import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.domain.repository.address.AddressRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository,
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(address: Address) =
        addressRepository.updateAddress(usersRepository.getSavedUserToken() ?: "", address)
}