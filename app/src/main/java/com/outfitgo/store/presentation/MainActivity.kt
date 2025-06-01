package com.outfitgo.store.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.presentation.home.HomeScreen
import com.outfitgo.store.presentation.login.LoginScreen
import com.outfitgo.store.presentation.login.LoginViewModel
import com.outfitgo.store.presentation.settings.view.CurrencyScreen
import com.outfitgo.store.presentation.settings.view.SettingsScreen
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OutfitGoTheme {
 /*               val viewModel: LoginViewModel = hiltViewModel()
                val state =  viewModel.state.collectAsStateWithLifecycle().value
                LoginScreen(
                    state = state,
                    onIntent = viewModel::processIntent,
                    effectFlow = viewModel.effect,
                    modifier = Modifier.fillMaxSize()
                )*/
                HomeScreen()
//                CurrencyScreen()
//                SettingsScreen()
            }
        }

    }
}
