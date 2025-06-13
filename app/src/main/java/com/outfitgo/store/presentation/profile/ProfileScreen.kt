package com.outfitgo.store.presentation.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.R
import com.outfitgo.store.domain.model.User
import com.outfitgo.store.presentation.profile.components.ProfileItem
import com.outfitgo.store.presentation.profile.components.UnAuthorizedScreen
import com.outfitgo.store.presentation.profile.components.UserCard
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onAboutUsClicked: () -> Unit,
    onAddressesClicked: () -> Unit,
    onOrdersClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onWishlistClicked: () -> Unit,
    onLogoutSuccess: () -> Unit,
    onLoginClicked: () -> Unit,
) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    ProfileScreenContent(
        state = state.value,
        onEvent = { intent ->
            when (intent) {
                ProfileIntent.GoToAboutUs -> onAboutUsClicked()
                ProfileIntent.GoToAddresses -> onAddressesClicked()
                ProfileIntent.GoToOrders -> onOrdersClicked()
                ProfileIntent.GoToSettings -> onSettingsClicked()
                ProfileIntent.GoToWishlist -> onWishlistClicked()
                ProfileIntent.GoToLogin -> onLoginClicked()
                else -> viewModel.processIntent(intent)
            }
        },
        effectFlow = viewModel.effect,
        modifier = modifier,
        onLogoutSuccess = onLogoutSuccess
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenContent(
    state: ProfileState,
    onEvent: (ProfileIntent) -> Unit,
    effectFlow: SharedFlow<ProfileEffect>,
    onLogoutSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {

    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onEvent(ProfileIntent.LoadProfile)
    }
    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is ProfileEffect.SendSnackBar -> snackbarHostState.showSnackbar(message = effect.msg)
                ProfileEffect.LogoutSuccess -> onLogoutSuccess()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.profile)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (state.isAuthenticated) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // user card
                    UserCard(state.user, modifier = Modifier.fillMaxSize())

                    Spacer(Modifier.height(8.dp))


                    // body section

                    ProfileItem(
                        icon = Icons.Outlined.LocationOn,
                        title = "Addresses",
                        onClick = {
                            onEvent(ProfileIntent.GoToAddresses)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )



                    ProfileItem(
                        icon = Icons.Outlined.AddBox,
                        title = stringResource(R.string.orders),
                        onClick = {
                            onEvent(ProfileIntent.GoToOrders)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ProfileItem(
                        icon = Icons.Outlined.FavoriteBorder,
                        title = stringResource(R.string.wishlist),
                        onClick = {
                            onEvent(ProfileIntent.GoToWishlist)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ProfileItem(
                        icon = Icons.Outlined.Settings,
                        title = "Settings",
                        onClick = {
                            onEvent(ProfileIntent.GoToSettings)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )


                    ProfileItem(
                        icon = Icons.Outlined.Info,
                        title = "About Us",
                        onClick = {
                            onEvent(ProfileIntent.GoToAboutUs)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ProfileItem(
                        icon = Icons.AutoMirrored.Outlined.Logout,
                        title = "Logout",
                        onClick = {
                            showConfirmationDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                UnAuthorizedScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    onClickLogin = { onEvent(ProfileIntent.GoToLogin) }
                )
            }
        }
    }


    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                showConfirmationDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmationDialog = false
                        onEvent(ProfileIntent.Logout)
                    },
                ) { Text("Logout", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                Button(onClick = {
                    showConfirmationDialog = false
                }) { Text("Cancel") }
            },
            title = {
                Text("Logout!")
            },
            text = {
                Text("you are about to Logout, are you sure?")
            }
        )
    }


}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    OutfitGoTheme {
        ProfileScreenContent(
            state = ProfileState(
                user = User(
                    id = "ID",
                    firstname = "Boody",
                    lastname = "Ahmed",
                    displayName = "Boody Ahmed",
                    email = "boodyyahmed@gmail.com"
                )
            ),
            onEvent = {},
            effectFlow = MutableSharedFlow<ProfileEffect>(),
            modifier = Modifier.fillMaxSize(),
            onLogoutSuccess = { },
        )
    }
}

