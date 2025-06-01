package com.outfitgo.store.presentation.productdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.domain.usecase.cart.AddProductToCartUseCase
import com.outfitgo.store.domain.usecase.products.GetProductByIdUseCase
import com.outfitgo.store.domain.usecase.wishlist.AddProductToWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val addProductToWishlistUseCase: AddProductToWishlistUseCase
): ViewModel() {

    private val _state: MutableStateFlow<ProductDetailsState> = MutableStateFlow(ProductDetailsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProductDetailsEffect>()
    val effect = _effect.asSharedFlow()


    fun processIntent(intent: ProductDetailsIntent) {
        when (intent) {
            is ProductDetailsIntent.AddToCart -> addToCart(intent.productId)
            is ProductDetailsIntent.AddToWishlist -> addToWishList(intent.productId)
            ProductDetailsIntent.ShowAllReviews -> {
                viewModelScope.launch {
                    _effect.emit(ProductDetailsEffect.GoToReviewsScreen)
                }
            }

            is ProductDetailsIntent.GetProductById -> loadProduct(intent.productId)
        }
    }

    private fun loadProduct(productId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val product = getProductByIdUseCase.execute(productId)
                _state.update { it.copy(product = product, isLoading = false) }
            } catch (ex: Exception) {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(ProductDetailsEffect.SendSnackBar(ex.localizedMessage ?: "ERROR"))
            }
        }
    }

    private fun addToCart(productId: String) {
        viewModelScope.launch {
            _effect.emit(ProductDetailsEffect.SendSnackBar("added $productId to Cart"))
        }
    }

    private fun addToWishList(productId: String) {
        viewModelScope.launch {
            _effect.emit(ProductDetailsEffect.SendSnackBar("added $productId to Wishlist"))
        }
    }


}