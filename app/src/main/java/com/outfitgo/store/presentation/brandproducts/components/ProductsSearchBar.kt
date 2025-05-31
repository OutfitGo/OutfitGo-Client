package com.outfitgo.store.presentation.brandproducts.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.outfitgo.store.R

@Composable
fun ProductsSearchBar(
    onQueryChanged: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        value = searchQuery,
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        onValueChange = {
            searchQuery = it
            onQueryChanged(it)
        },
        placeholder = {
            Text(
                text = stringResource(R.string.search)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}

@Preview
@Composable
private fun SearchBarPreview() {
    ProductsSearchBar(
        onQueryChanged = {}
    )
}