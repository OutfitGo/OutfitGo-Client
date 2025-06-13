package com.outfitgo.store.presentation.address

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.domain.usecase.address.CreateAddressUseCase
import com.outfitgo.store.domain.usecase.address.DeleteAddressUseCase
import com.outfitgo.store.domain.usecase.address.GetDefaultAddressUseCase
import com.outfitgo.store.domain.usecase.address.GetUserAddressesUseCase
import com.outfitgo.store.domain.usecase.address.UpdateAddressUseCase
import com.outfitgo.store.domain.usecase.address.UpdateDefaultAddressUseCase
import com.outfitgo.store.domain.usecase.auth.GetSavedUserTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val getAddressesUseCase: GetUserAddressesUseCase,
    private val getDefaultAddressUseCase: GetDefaultAddressUseCase,
    private val deleteAddressUseCase: DeleteAddressUseCase,
    private val addAddressUseCase: CreateAddressUseCase,
    private val updateAddressUseCase: UpdateAddressUseCase,
    private val setDefaultAddressUseCase: UpdateDefaultAddressUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AddressState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AddressEffect>()
    val effect = _effect.asSharedFlow()

    fun processIntent(intent: AddressIntent) {
        when (intent) {
            is AddressIntent.getAdrresses -> {
                loadAddressesWithDefault()
            }

            is AddressIntent.deleteAddress -> {
                deleteAddress(intent.id)
            }

            is AddressIntent.requestDeleteAddress -> {
                viewModelScope.launch {
                    _effect.emit(AddressEffect.onAddressRemove(intent.id))
                }
            }

            is AddressIntent.AddNewAddress -> {
                intent.run { addAddress(Address("", firstName, lastName, line, city, false)) }
            }

            is AddressIntent.updateAddress -> {
                updateAddress(intent.address)
            }

            is AddressIntent.updateDefaultAddress -> {
                updateDefaultAddress(intent.addressId)
            }
        }
    }

    private fun loadAddressesWithDefault() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            val addresses = getAddressesUseCase()
            val defaultAddress = getDefaultAddressUseCase()

            val updatedList = addresses.map {
                it.copy(isDefault = getPureId(it.id) == getPureId(defaultAddress.id))
            }

            _state.update { it.copy(addresses = updatedList, isLoading = false) }
        }
    }

    private fun getPureId(fullId: String): String {
        return fullId.substringAfterLast("/").substringBefore("?")
    }

    private fun deleteAddress(id: String) {
        viewModelScope.launch {
            try {
                deleteAddressUseCase(addressId = id)
                _state.update { it.copy(addresses = it.addresses.filter { it.id != id }) }
                _effect.emit(AddressEffect.onAddressRemoved)
            } catch (e: Exception) {
                Log.d("``TAG``", "deleteAddress: ${e.message}")
            }
        }
    }

    private fun addAddress(address: Address) {
        viewModelScope.launch {
            try {
                addAddressUseCase(address = address)
                _effect.emit(AddressEffect.onAddAddress)
            } catch (e: Exception) {
                Log.d("``TAG``", "addAddress: ${e.message}")
            }
        }
    }

    private fun updateAddress(updatedAddress: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                updateAddressUseCase(updatedAddress)
                if (updatedAddress.isDefault) {
                    updateDefaultAddress(updatedAddress.id)
                }
                _effect.emit(AddressEffect.onAddressUpdated)
            } catch (e: Exception) {
                _effect.emit(
                    AddressEffect.onAddressUpdateError(
                        e.message ?: "something happened wrong"
                    )
                )
            }
        }
    }

    private fun updateDefaultAddress(addressId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                setDefaultAddressUseCase(addressId = addressId)
            } catch (e: Exception) {
                _effect.emit(
                    AddressEffect.onAddressUpdateError(
                        e.message ?: "something happened wrong"
                    )
                )
            }
        }
    }
}