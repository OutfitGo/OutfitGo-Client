package com.outfitgo.store.presentation.address

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    first:String?,
    last:String?,
    onBack: () -> Unit,
    onPickFromMap: (String,String) -> Unit,
    lineFromMap:String?,
    cityFromMap:String?,
    viewModel: AddressViewModel = hiltViewModel()
) {
    var firstName by remember { mutableStateOf(first?:"") }
    var lastName by remember { mutableStateOf(last?:"") }
    var addressLine by remember { mutableStateOf(lineFromMap?:"") }
    var city by remember { mutableStateOf(cityFromMap?:"") }
    val context= LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                is AddressEffect.onAddressRemove -> {}

                is AddressEffect.onAddressRemoved -> {}
                is AddressEffect.onAddressUpdateError -> {}
                AddressEffect.onAddressUpdated -> {}
                AddressEffect.onAddAddress -> {
                    Toast.makeText(context,"Address Added Successfully",Toast.LENGTH_SHORT).show()
                    onBack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Address") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors =TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = addressLine,
                onValueChange = { addressLine = it },
                label = { Text("Address Line") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(
                onClick = { onPickFromMap(firstName,lastName) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Pick from Map")
            }

            Spacer(modifier = Modifier.weight(1f))

            val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
                    addressLine.isNotBlank() && city.isNotBlank()
            Button(
                onClick = {
                    viewModel.processIntent(
                        AddressIntent.AddNewAddress(
                            firstName = firstName,
                            lastName = lastName,
                            line = addressLine,
                            city = city
                        )
                    )
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "Add Address")
            }
        }
    }
}
