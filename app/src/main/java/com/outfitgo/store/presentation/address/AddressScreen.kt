package com.outfitgo.store.presentation.address

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.presentation.components.EmptyState


@Composable
fun AddressScreen(
    viewModel: AddressViewModel = hiltViewModel(),
    onEditAddress: (Address) -> Unit,
    onNavToAddAddressScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val showRemoveDialog = remember { mutableStateOf(false) }
    val itemToRemove = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.processIntent(AddressIntent.getAdrresses)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                is AddressEffect.onAddressRemove -> {
                    itemToRemove.value = effect.id
                    showRemoveDialog.value = true
                }

                is AddressEffect.onAddressRemoved -> {

                }

                is AddressEffect.onAddressUpdateError -> {}
                AddressEffect.onAddressUpdated -> {}
                AddressEffect.onAddAddress -> {}
            }
        }
    }

    if (showRemoveDialog.value) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog.value = false },
            title = { Text("Remove Address") },
            text = { Text("Are you sure you want to remove this Address?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRemoveDialog.value = false
                        viewModel.processIntent(AddressIntent.deleteAddress(itemToRemove.value))
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            AddressScreenContent(
                addresses = state.addresses,
                onNavToAddAddressScreen = {
                    onNavToAddAddressScreen()
                },
                onEditAddress = onEditAddress,
                onRequestDelete = {
                    viewModel.processIntent(AddressIntent.requestDeleteAddress(it))
                }
            )

        }
    }
}

@Composable
fun AddressScreenContent(
    addresses: List<Address>,
    onNavToAddAddressScreen: () -> Unit,
    onEditAddress: (Address) -> Unit,
    onRequestDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        floatingActionButton = {
            AddAddressButton(onNavToAddAddressScreen = onNavToAddAddressScreen)
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("Addresses", style = MaterialTheme.typography.titleLarge)
            if (addresses.isEmpty()){
                EmptyState(
                    imgRes = R.drawable.ic_address_empty, mainText = "Empty Address List",
                    description = "Add some Address"
                )
            }else{
                AddressesList(
                    addresses = addresses,
                    onEditAddress = onEditAddress,
                    onRequestDelete = onRequestDelete,
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
            }
        }
    }
}

@Composable
private fun AddressesList(
    addresses: List<Address>,
    onEditAddress: (Address) -> Unit,
    onRequestDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(addresses) { address ->
            AddressRow(address, onEditAddress, onRequestDelete)
        }
    }
}

@Composable
private fun AddressRow(
    address: Address,
    onEditAddress: (Address) -> Unit,
    onRequestDelete: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onRequestDelete(address.id)
                true
            } else {
                false
            }
        }
    )
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(MaterialTheme.colorScheme.background)
                .clickable {
                    onEditAddress(address)
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier.fillMaxSize()

            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_address_single),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                ) {
                    Text(
                        text = address.run { "$firstName $lastName" },
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = address.city,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (address.isDefault) {
                        Text(
                            text = "Default",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(R.drawable.ic_next),
                    contentDescription = ""
                )
            }
            HorizontalDivider()
        }
    }
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue != SwipeToDismissBoxValue.EndToStart &&
            dismissState.targetValue == SwipeToDismissBoxValue.Settled
        ) {
            dismissState.reset()
        }
    }
}

@Composable
fun AddAddressButton(onNavToAddAddressScreen: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = onNavToAddAddressScreen,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_add_address),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.background
        )
    }
}