package com.outfitgo.store.domain.usecase.address

import com.outfitgo.store.domain.repository.address.AddressRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class GetDefaultAddressUseCase @Inject constructor(
    private val repository: AddressRepository,
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke() =
        repository.getDefaultAddress(usersRepository.getSavedUserToken() ?: "")
}