package com.outfitgo.store.presentation.address

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf(cityFromMap ?: "Cairo") }
    val state by viewModel.state.collectAsState()
    val context= LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(AddressIntent.getCities)
    }

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
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()

            )

            OutlinedTextField(
                value = addressLine,
                onValueChange = { addressLine = it },
                label = { Text("Address Line") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCity,
                    onValueChange = { selectedCity = it },
                    readOnly = true,
                    label = { Text("City") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    containerColor = MaterialTheme.colorScheme.background,
                    onDismissRequest = { expanded = false }
                ) {
                    state.cities.forEach { cityOption ->
                        DropdownMenuItem(
                            text = { Text(cityOption) },
                            onClick = {
                                selectedCity = cityOption
                                expanded = false
                            }
                        )
                    }
                }
            }


            TextButton(
                onClick = { onPickFromMap(firstName,lastName) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Pick from Map")
            }

            Spacer(modifier = Modifier.weight(1f))

            val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
                    addressLine.isNotBlank() && selectedCity.isNotBlank()
            Button(
                onClick = {
                    viewModel.processIntent(
                        AddressIntent.AddNewAddress(
                            firstName = firstName,
                            lastName = lastName,
                            line = addressLine,
                            city = selectedCity
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
