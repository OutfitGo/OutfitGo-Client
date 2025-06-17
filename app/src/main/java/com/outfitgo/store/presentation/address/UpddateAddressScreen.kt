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
import com.outfitgo.store.domain.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateAddressScreen(
    onBack: () -> Unit,
    addressId: String,
    addressFirstName: String,
    addressLastName: String,
    addressLine: String,
    addressCity: String,
    addressIsDefault: Boolean,
    onPickFromMap: () -> Unit,
    lineFromMap: String?,
    cityFromMap: String?,
    viewModel: AddressViewModel = hiltViewModel()
) {
    var firstName by remember { mutableStateOf(addressFirstName) }
    var lastName by remember { mutableStateOf(addressLastName) }
    var addressLine by remember { mutableStateOf(lineFromMap ?: addressLine) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf(cityFromMap ?: "Cairo") }
    val state by viewModel.state.collectAsState()
    var isDefault by remember { mutableStateOf(addressIsDefault) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(AddressIntent.getCities)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                is AddressEffect.onAddressRemove -> {}

                is AddressEffect.onAddressRemoved -> {}
                is AddressEffect.onAddressUpdateError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                AddressEffect.onAddressUpdated -> {
                    Toast.makeText(context, "Address Updated Successfully", Toast.LENGTH_SHORT)
                        .show()
                    onBack()
                }

                AddressEffect.onAddAddress -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Address") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
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
                onClick = onPickFromMap,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Pick from Map")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isDefault,
                    onCheckedChange = { isDefault = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Make this my default address")
            }

            Spacer(modifier = Modifier.weight(1f))

            val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
                    addressLine.isNotBlank() && selectedCity.isNotBlank()
            Button(
                onClick = {
                    viewModel.processIntent(
                        AddressIntent.updateAddress(
                            Address(
                                addressId,
                                firstName,
                                lastName,
                                addressLine,
                                selectedCity,
                                isDefault
                            )
                        )
                    )
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Update Address")
            }
        }
    }
}
