package com.outfitgo.store.presentation.mappiker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.outfitgo.store.core.util.ActionResult
import com.outfitgo.store.domain.repository.address.AddressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class MapPickerViewModel @Inject constructor(
    private val repo: AddressRepository,
    private val placesClient: PlacesClient
) : ViewModel() {

    private val _searchQuery = MutableSharedFlow<String>(replay = 1)

    private val _searchResults = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _predictedPlaceLatLng = MutableStateFlow<LatLng?>(null)
    val predictedPlaceLatLng = _predictedPlaceLatLng.asStateFlow()

    private val _isPlaceSaved = MutableStateFlow(ActionResult.IDLE)
    val isPlaceSaved = _isPlaceSaved.asStateFlow()

    private val _currentAddress = MutableStateFlow("")
    val currentAddress = _currentAddress.asStateFlow()

    private val _currentCity = MutableStateFlow("")
    val currentCity = _currentCity.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _searchQuery
                .debounce(1000)
                .distinctUntilChanged()
                .collectLatest { query -> searchForPlaces(placesClient, query) }
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        viewModelScope.launch {
            _searchQuery.emit(newQuery)
        }
    }

    fun onPredictionSelected(placeId: String,placeName:String,city:String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.fetchPlaceDetails(
                placesClient = placesClient,
                placeId = placeId
            ).addOnSuccessListener { response ->
                _predictedPlaceLatLng.value = response.place.latLng
                _currentAddress.value = placeName
                _currentCity.value= city
            }.addOnFailureListener {
                _predictedPlaceLatLng.value = null
            }
        }
    }

    fun searchForPlaces(
        placesClient: PlacesClient,
        query: String
    ) {
        repo.fetchPlacePredictions(
            placesClient = placesClient,
            query = query
        ).addOnSuccessListener { response ->
            _searchResults.value = response.autocompletePredictions
        }.addOnFailureListener {
            _searchResults.value = emptyList()
        }
    }

    fun updateCurrentAddress(newAddress: String, city: String) {
        _currentAddress.value = newAddress
        _currentCity.value = city
    }
}
