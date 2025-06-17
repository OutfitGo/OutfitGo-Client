package com.outfitgo.store.data.datasource.remote.address

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.outfitgo.store.data.repository.address.AddressRemoteDataSource
import com.outfitgo.store.domain.model.Address


class FakeAddressRemoteDataSource(
    private var addresses: MutableList<Address> = mutableListOf(),
    private var defaultAddressId: String? = null
) : AddressRemoteDataSource {

    override suspend fun getAddresses(token: String): List<Address> {
        return addresses
    }

    override suspend fun getDefaultAddress(token: String): Address {
        return addresses.find { it.id == defaultAddressId }
            ?: throw IllegalStateException("No default address set")
    }

    override suspend fun createAddress(token: String, address: Address) {
        addresses.add(address)
    }

    override suspend fun updateAddress(token: String, address: Address) {
        val index = addresses.indexOfFirst { it.id == address.id }
        if (index != -1) {
            addresses[index] = address
        } else {
            throw IllegalArgumentException("Address not found")
        }
    }

    override suspend fun deleteAddress(token: String, addressId: String) {
        addresses.removeAll { it.id == addressId }
        if (defaultAddressId == addressId) {
            defaultAddressId = null
        }
    }

    override suspend fun setDefaultAddress(token: String, addressId: String) {
        if (addresses.any { it.id == addressId }) {
            defaultAddressId = addressId
        } else {
            throw IllegalArgumentException("Address not found")
        }
    }
    override fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse> {
        TODO("Not yet implemented")
    }

    override fun fetchPlaceDetails(
        placesClient: PlacesClient,
        placeId: String
    ): Task<FetchPlaceResponse> {
        TODO("Not yet implemented")
    }
}