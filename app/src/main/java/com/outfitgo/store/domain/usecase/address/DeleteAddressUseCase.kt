package com.outfitgo.store.domain.usecase.address

import com.outfitgo.store.domain.repository.address.AddressRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class DeleteAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository,
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(addressId: String) =
        addressRepository.deleteAddress(usersRepository.getSavedUserToken() ?: "", addressId)
}