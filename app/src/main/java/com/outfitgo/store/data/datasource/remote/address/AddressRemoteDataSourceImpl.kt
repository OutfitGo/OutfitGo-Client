package com.outfitgo.store.data.datasource.remote.address

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.outfitgo.store.data.mappers.toDomain
import com.outfitgo.store.data.repository.address.AddressRemoteDataSource
import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.storefront.CreateAddressMutation
import com.outfitgo.store.storefront.DeleteAddressMutation
import com.outfitgo.store.storefront.GetAddressQuery
import com.outfitgo.store.storefront.SetDefaultAddressMutation
import com.outfitgo.store.storefront.UpdateAddressMutation
import javax.inject.Inject

class AddressRemoteDataSourceImpl @Inject constructor(private val client: ApolloClient) :
    AddressRemoteDataSource {
    override suspend fun getAddresses(token: String): List<Address> {
        val response = client.query(GetAddressQuery(token)).execute()
        if (response.hasErrors()) {
            throw Exception(response.exception?.message)
        }
        if (response.data?.customer?.addresses?.edges.isNullOrEmpty()) {
            return emptyList()
        }
        return response.data?.customer?.addresses?.toDomain() ?: emptyList()
    }

    override suspend fun getDefaultAddress(token: String): Address {
        val response = client.query(GetAddressQuery(token)).execute()
        if (response.hasErrors()) {
            throw Exception(response.exception?.message)
        }
        if (response.data?.customer?.addresses?.edges.isNullOrEmpty()) {
            return Address("", "", "", "","", true)
        }
        return response.data?.customer?.defaultAddress?.toDomain() ?: Address("", "", "", "", "",true)
    }

    override suspend fun createAddress(token: String, address: Address) {
        val response = client.mutation(
            CreateAddressMutation(
                token,
                address.firstName,
                address.lastName,
                address.line,
                address.city,
                "Egypt"
            )
        ).execute()
        if (response.hasErrors()) {
            throw Exception(response.exception?.message)
        }
    }

    override suspend fun updateAddress(token: String, address: Address) {
        val response = client.mutation(
            UpdateAddressMutation(
                token,
                address.id,
                address.firstName,
                address.lastName,
                address.line,
                address.city,
                Optional.present(""),
                Optional.present("")
            )
        ).execute()
        if (response.hasErrors()) {
            throw Exception(response.exception?.message)
        }
    }

    override suspend fun deleteAddress(token: String, addressId: String) {
        val response = client.mutation(DeleteAddressMutation(token, addressId)).execute()
        if (response.hasErrors()) {
            throw Exception(response.exception?.message)
        }
    }

    override suspend fun setDefaultAddress(token: String, addressId: String) {
        val response = client.mutation(SetDefaultAddressMutation(token, addressId)).execute()
        if (response.hasErrors()) {
            throw Exception(response.exception?.message)
        }
    }

    override fun fetchPlacePredictions(
        placesClient: PlacesClient,
        query: String
    ): Task<FindAutocompletePredictionsResponse> {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()
        return placesClient.findAutocompletePredictions(request)
    }

    override fun fetchPlaceDetails(
        placesClient: PlacesClient,
        placeId: String
    ): Task<FetchPlaceResponse> {
        val request = FetchPlaceRequest.newInstance(
            placeId,
            listOf(Place.Field.LAT_LNG)
        )

        return placesClient.fetchPlace(request)
    }
}