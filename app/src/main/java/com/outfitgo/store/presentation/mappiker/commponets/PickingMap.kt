package com.outfitgo.store.presentation.mappiker.commponets

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.outfitgo.store.core.util.MapUtil

@Composable
fun PickingMap(
    markerState: MarkerState,
    cameraPositionState: CameraPositionState,
    onMapClick: (String,String) -> Unit
) {
    var placeName by remember { mutableStateOf("") }
    var cityName by remember { mutableStateOf("") }
    val context = LocalContext.current

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(mapType = MapType.HYBRID),
        uiSettings = MapUiSettings().copy(zoomControlsEnabled = false),
        onMapClick = { newPosition ->
            markerState.position = newPosition
            placeName = MapUtil.getLocationAddressLine(
                context = context,
                latitude = markerState.position.latitude,
                longitude = markerState.position.longitude
            )?.getAddressLine(0) ?: "Unknown Place"

            cityName = MapUtil.getLocationAddressLine(
                context = context,
                latitude = markerState.position.latitude,
                longitude = markerState.position.longitude
            )?.locality ?: "Unknown Place"

            markerState.showInfoWindow()
            onMapClick(placeName,cityName)
        }
    ) {
        MarkerInfoWindowContent(
            state = markerState,
            onClick = {
                markerState.hideInfoWindow()
                false
            },
        ) {
            Text(
                text = placeName,
                fontWeight = FontWeight.Bold
            )
        }
    }
}