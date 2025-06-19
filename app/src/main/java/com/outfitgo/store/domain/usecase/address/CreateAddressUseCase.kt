package com.outfitgo.store.domain.usecase.address

import android.util.Log
import com.outfitgo.store.core.util.exceptions.MissingUserTokenException
import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.domain.repository.address.AddressRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import javax.inject.Inject

class CreateAddressUseCase @Inject constructor(
    private val addressRepository: AddressRepository,
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(address: Address): Unit {
        Log.d("``TAG``", "invoke: ${address}")
        val token = usersRepository.getSavedUserToken()
            ?: throw MissingUserTokenException()
        return addressRepository.createAddress(token, address)
    }
}
