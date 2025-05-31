package com.outfitgo.store.presentation


import ProductDetailsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.outfitgo.store.presentation.productdetails.ProductDetailsViewModel
import com.outfitgo.store.presentation.ui.theme.OutfitGoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OutfitGoTheme {
                val vm: ProductDetailsViewModel = hiltViewModel()
                val state = vm.state.collectAsStateWithLifecycle()
                val id = "gid://shopify/Product/9762760556824"
                ProductDetailsScreen(
                    state = state.value,
                    onIntent = vm::processIntent,
                    effect = vm.effect,
                    modifier = Modifier.fillMaxSize(),
                    productId = id,
                )

            }
        }

    }
}
