package com.outfitgo.store.presentation.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.Const
import com.outfitgo.store.domain.usecase.cart.AddBuyerToCartUseCase
import com.outfitgo.store.domain.usecase.cart.CreateCartUseCase
import com.outfitgo.store.domain.usecase.cart.SaveCartIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val createCartUseCase: CreateCartUseCase,
    private val addBuyerToCartUseCase: AddBuyerToCartUseCase,
    private val saveCartIdUseCase: SaveCartIdUseCase,
) : ViewModel() {
    init {
        cartInit()
    }

    private fun cartInit() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cartId = createCartUseCase.execute()
                Const.cartId = cartId
                saveCartIdUseCase.execute(cartId)
                addBuyerToCartUseCase.execute(cartId)
                Log.d("``Tag``","new cart created")
            } catch (e: Exception) {
                Log.d("```TAG```", "cartInit: ${e.message}")
            }
        }
    }
}