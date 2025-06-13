package com.outfitgo.store.presentation.mappiker.commponets


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.libraries.places.api.model.AutocompletePrediction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesSearchBar(
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit,
    predictions: List<AutocompletePrediction>,
    onPlaceSelected: (String,String,String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isDropdownExpanded && predictions.isNotEmpty(),
        onExpandedChange = { isDropdownExpanded = it }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryEditable)
                .fillMaxWidth(),
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                onQueryChanged(it)
                isDropdownExpanded = it.isNotEmpty()
            },
            placeholder = {
                Text(text = "Search for a city")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.White
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            modifier = Modifier.animateContentSize(),
            shape = RoundedCornerShape(12.dp),
            containerColor = Color.White,
            expanded = isDropdownExpanded && predictions.isNotEmpty(),
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            predictions.forEach { prediction ->
                DropdownMenuItem(
                    text = { Text(prediction.getFullText(null).toString()) },
                    onClick = {
                        onPlaceSelected(
                            prediction.placeId,
                            prediction.getFullText(null).toString(),
                            prediction.getPrimaryText(null).toString()
                        )
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }

}