package com.outfitgo.store.domain.repository.address

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.outfitgo.store.domain.model.Address

interface AddressRepository {
    suspend fun getAddresses(token: String): List<Address>
    suspend fun getDefaultAddress(token: String): Address
    suspend fun createAddress(token: String, address: Address)
    suspend fun updateAddress(token: String, address: Address)
    suspend fun deleteAddress(token: String, addressId: String)
    suspend fun setDefaultAddress(token: String, addressId: String)
    fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse>

    fun fetchPlaceDetails(placesClient: PlacesClient, placeId: String): Task<FetchPlaceResponse>
}