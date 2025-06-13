package com.outfitgo.store.data.repository.address

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.domain.repository.address.AddressRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(private val addressRemoteDataSource: AddressRemoteDataSource) :
    AddressRepository {
    override suspend fun getAddresses(token: String): List<Address> {
        return addressRemoteDataSource.getAddresses(token)
    }

    override suspend fun getDefaultAddress(token: String): Address {
        return addressRemoteDataSource.getDefaultAddress(token)
    }

    override suspend fun createAddress(token: String, address: Address) {
        addressRemoteDataSource.createAddress(token, address)
    }

    override suspend fun updateAddress(token: String, address: Address) {
        addressRemoteDataSource.updateAddress(token, address)
    }

    override suspend fun deleteAddress(token: String, addressId: String) {
        addressRemoteDataSource.deleteAddress(token, addressId)
    }

    override suspend fun setDefaultAddress(token: String, addressId: String) {
        addressRemoteDataSource.setDefaultAddress(token, addressId)
    }

    override fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse> {
        return addressRemoteDataSource.fetchPlacePredictions(placesClient, query)
    }
    override fun fetchPlaceDetails(
        placesClient: PlacesClient,
        placeId: String
    ): Task<FetchPlaceResponse> {
        return addressRemoteDataSource.fetchPlaceDetails(placesClient, placeId)
    }
}