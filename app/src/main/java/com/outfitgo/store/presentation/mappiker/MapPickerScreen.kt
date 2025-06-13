package com.outfitgo.store.presentation.mappiker


import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.outfitgo.store.core.util.ActionResult
import com.outfitgo.store.presentation.mappiker.commponets.PickingMap
import com.outfitgo.store.presentation.mappiker.commponets.PlacesSearchBar
import com.outfitgo.store.presentation.mappiker.commponets.SaveButton
import kotlinx.coroutines.launch

@Composable
fun MapPickerScreen(
    viewModel: MapPickerViewModel,
    initialLat: Double,
    initialLong: Double,
    onSaveClicked: (String,String) -> Unit,
    onNavigateUp: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var markerState = remember { MarkerState(position = LatLng(initialLat, initialLong)) }
    val cameraPositionState = rememberCameraPositionState {
        scope.launch {
            animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(initialLat, initialLong),
                    10f
                )
            )
        }
    }

    val predictions = viewModel.searchResults.collectAsStateWithLifecycle()
    val selectedPredictionLatLng = viewModel.predictedPlaceLatLng.collectAsStateWithLifecycle()
    val savePlaceState = viewModel.isPlaceSaved.collectAsStateWithLifecycle()
    val currentAddress by viewModel.currentAddress.collectAsStateWithLifecycle()
    val currentCity by viewModel.currentCity.collectAsStateWithLifecycle()
    PlacePickerScreenContent(
        markerState = markerState,
        savePlaceState = savePlaceState.value,
        cameraPositionState = cameraPositionState,
        predictions = predictions.value,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onPredictionSelected = viewModel::onPredictionSelected,
        onSaveClicked = {
            onSaveClicked(currentAddress,currentCity)
        },
        onMapClicked= viewModel::updateCurrentAddress
    )

    LaunchedEffect(key1 = selectedPredictionLatLng.value) {
        selectedPredictionLatLng.value?.let {
            markerState.position = it
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(it, 10f))
        }
    }

    LaunchedEffect(key1 = savePlaceState.value) {
        when (savePlaceState.value) {
            ActionResult.FAILED -> {
                Toast.makeText(context, "save address field", Toast.LENGTH_SHORT).show()
            }

            ActionResult.COMPLETED -> {
                Toast.makeText(context, "address saved successfully", Toast.LENGTH_SHORT).show()
                onNavigateUp()
            }

            else -> {}
        }
    }
}

@Composable
private fun PlacePickerScreenContent(
    markerState: MarkerState,
    savePlaceState: ActionResult,
    cameraPositionState: CameraPositionState,
    predictions: List<AutocompletePrediction>,
    onSearchQueryChanged: (String) -> Unit,
    onPredictionSelected: (String,String,String) -> Unit,
    onSaveClicked: () -> Unit,
    onMapClicked: (String,String) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PickingMap(
            markerState = markerState,
            cameraPositionState = cameraPositionState,
            onMapClick = onMapClicked
        )

        PlacesSearchBar(
            modifier = Modifier
                .padding(vertical = 56.dp, horizontal = 24.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            onQueryChanged = onSearchQueryChanged,
            predictions = predictions,
            onPlaceSelected = onPredictionSelected
        )

        SaveButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            savePlaceState = savePlaceState,
            onSaveClicked = onSaveClicked
        )
    }
}