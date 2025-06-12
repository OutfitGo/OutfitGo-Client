package com.outfitgo.store.presentation.wishlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.usecase.auth.GetSavedUserIdUseCase
import com.outfitgo.store.domain.usecase.wishlist.GetAllProductsFromWishlistUseCase
import com.outfitgo.store.domain.usecase.wishlist.RemoveProductFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "WishlistViewModel"

@HiltViewModel
class WishlistViewModel @Inject constructor (
    private val getAllProductsFromWishlistUseCase: GetAllProductsFromWishlistUseCase,
    private val removeProductFromWishlistUseCase: RemoveProductFromWishlistUseCase,
    private val getSavedUserIdUseCase: GetSavedUserIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(WishlistUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<WishlistEffect>()
    val effect = _effect.asSharedFlow()

    lateinit var userId: String

    init {
        viewModelScope.launch {
            userId = getSavedUserIdUseCase.execute() ?: ""
            loadProducts()
        }
    }

    fun processIntent(intent: WishlistIntent) {
        when(intent) {
            is WishlistIntent.AddProductToCart -> addProductToCart(intent.product)
            WishlistIntent.GetAllWishlistProducts -> loadProducts()
            WishlistIntent.RefreshWishlistProducts -> refreshProducts()
            is WishlistIntent.RemoveProduct -> removeProduct(intent.product)
            else -> Unit
        }
    }

    private fun refreshProducts() {
        Log.i(TAG, "refreshProducts: ")
        _state.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            try {
                safeAuth(
                    authBlock = {
                        val products = getAllProductsFromWishlistUseCase.execute(userId)
                        _state.update { it.copy(isRefreshing = false, products = products) }
                    },
                    unAuthBlock = {
                        _state.update { it.copy(isRefreshing = false) }
                        _effect.emit(WishlistEffect.SendSnackBar("You Must Be Authenticated"))
                    }
                )
            } catch (exp: Exception) {
                _state.update { it.copy(isRefreshing = false) }
                _effect.emit(WishlistEffect.SendSnackBar(exp.message ?: "ERROR"))
            }
        }
    }

    private fun removeProduct(product: Product) {
        viewModelScope.launch {
            try {
                removeProductFromWishlistUseCase.execute(userId, product.id)
                loadProducts()
            } catch (exp: Exception) {
                _effect.emit(WishlistEffect.SendSnackBar(exp.message ?: "ERROR while delete"))
            }

        }
    }

    private fun loadProducts() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                safeAuth(
                    authBlock = {
                        val products = getAllProductsFromWishlistUseCase.execute(userId)
                        _state.update { it.copy(isLoading = false, products = products) }
                    },
                    unAuthBlock = {
                        _state.update { it.copy(isLoading = false) }
                        _effect.emit(WishlistEffect.SendSnackBar("You Must Be Authenticated"))
                    }
                )


            } catch (exp: Exception) {
                _effect.emit(WishlistEffect.SendSnackBar(exp.message ?: "ERROR"))
            }
        }
    }

    private fun addProductToCart(product: Product) {

    }


    private fun safeAuth(authBlock: suspend () -> Unit, unAuthBlock: suspend () -> Unit) {
        viewModelScope.launch {
            if(userId.isNotBlank()) {
                authBlock()
            } else {
                unAuthBlock()
            }
        }

    }


}