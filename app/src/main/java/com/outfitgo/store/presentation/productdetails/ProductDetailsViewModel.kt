package com.outfitgo.store.presentation.productdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.Const
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.usecase.auth.GetSavedUserIdUseCase
import com.outfitgo.store.domain.usecase.cart.AddProductToCartUseCase
import com.outfitgo.store.domain.usecase.products.GetProductByIdUseCase
import com.outfitgo.store.domain.usecase.wishlist.AddProductToWishlistUseCase
import com.outfitgo.store.domain.usecase.wishlist.GetIsProductInWishlistUseCase
import com.outfitgo.store.domain.usecase.wishlist.RemoveProductFromWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ProductDetailsViewModel"

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase,
    private val addProductToWishlistUseCase: AddProductToWishlistUseCase,
    private val removeProductFromWishlistUseCase: RemoveProductFromWishlistUseCase,
    private val getSavedUserIdUseCase: GetSavedUserIdUseCase,
    private val getIsInWishlistUseCase: GetIsProductInWishlistUseCase
): ViewModel() {

    private val _state: MutableStateFlow<ProductDetailsState> = MutableStateFlow(ProductDetailsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProductDetailsEffect>()
    val effect = _effect.asSharedFlow()

    private lateinit var userId: String

    init {
        viewModelScope.launch {
            userId = getSavedUserIdUseCase.execute() ?: ""
            Log.i(TAG, "userId: $userId")
        }
    }


    fun processIntent(intent: ProductDetailsIntent) {
        when (intent) {
            is ProductDetailsIntent.AddToCart -> addToCart(intent.productVariantId)
            is ProductDetailsIntent.AddToWishlist -> addToWishList(intent.product)
            is ProductDetailsIntent.RemoveFromWishList -> removeFromWishlist(intent.productId)
            is ProductDetailsIntent.GetProductById -> loadProduct(intent.productId)
            is ProductDetailsIntent.SelectProductVariant -> _state.update { it.copy(selectedVariantId = intent.variant.id) }
            else -> Unit
        }
    }

    private fun removeFromWishlist(productId: String) {
        Log.i(TAG, "removeFromWishlist: started")
        runIfAuthenticated(
            authedBlock = {
                viewModelScope.launch {
                    try {
                        removeProductFromWishlistUseCase.execute(userId, productId)
                        _state.update { it.copy(isFavorite = false) }
                        _effect.emit(ProductDetailsEffect.SendSnackBar("removed from favorite"))
                    } catch (exp: Exception) {
                        _effect.emit(ProductDetailsEffect.SendSnackBar(exp.message ?: "Error while removing from wishlist"))
                    }
                }

            },
            unAuthedBlock = {
                viewModelScope.launch {
                    _effect.emit(ProductDetailsEffect.SendSnackBar("can't remove from wishlist in guest mode, please login first"))
                }
            }
        )
    }

    private fun loadProduct(productId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val product = getProductByIdUseCase.execute(productId)
                _state.update { it.copy(product = product, isLoading = false) }

                runIfAuthenticated(
                    authedBlock = {
                        viewModelScope.launch {
                            val isFav = getIsInWishlistUseCase.execute(userId, _state.value.product.id)
                            Log.i(TAG, "loadProduct: $productId isFav: $isFav")
                            _state.update { it.copy(isFavorite = isFav) }
                        }
                    },
                    unAuthedBlock = {}
                )

            } catch (ex: Exception) {
                _state.update { it.copy(isLoading = false) }
                _effect.emit(ProductDetailsEffect.SendSnackBar(ex.localizedMessage ?: "ERROR"))
            }
        }
    }

    private fun addToCart(productId: String) {
        runIfAuthenticated(
            authedBlock = {
                viewModelScope.launch {
                    try {
                        if (_state.value.selectedVariantId.isEmpty()) {
                            _effect.emit(ProductDetailsEffect.SendSnackBar("You Have to Choose Variant to be added to the cart"))
                        } else {
                            addProductToCartUseCase.execute(
                                Const.cartId,
                                1,
                                _state.value.selectedVariantId
                            )
                            _state.update { it.copy(isAddedToCart = true) }
                            _effect.emit(ProductDetailsEffect.SendSnackBar("added $productId to Cart"))
                        }
                    } catch (e: Exception) {
                        Log.d("``TAG``", "addToCart: ${e.message} id is $productId ")
                    }
                }
            },
            unAuthedBlock = {
                viewModelScope.launch {
                    _effect.emit(ProductDetailsEffect.SendSnackBar("You Can't add Products to cart in guest mode, please login first"))
                }
            }
        )
    }

    private fun addToWishList(product: Product) {
        runIfAuthenticated(
            authedBlock = {
                viewModelScope.launch {
                    try {
                        Log.i(TAG, "addToWishList: started")
                        addProductToWishlistUseCase.execute(userId, product)
                        _state.update { it.copy(isFavorite = true) }
                        Log.i(TAG, "addToWishList: added successfully")
                        _effect.emit(ProductDetailsEffect.SendSnackBar("added ${product.name} to your Wishlist"))
                    } catch (exp: Exception) {
                        _effect.emit(ProductDetailsEffect.SendSnackBar(exp.message ?: "ERROR while adding to wishlist"))
                    }
                }
            },
            unAuthedBlock = {
                viewModelScope.launch {
                    _effect.emit(ProductDetailsEffect.SendSnackBar("You Can't add Products to wishlist in guest mode, please login first"))
                }
            }
        )

    }

    private fun runIfAuthenticated(authedBlock: () -> Unit, unAuthedBlock: () -> Unit) {
        if(userId.isNotBlank()) {
            authedBlock()
        } else {
            unAuthedBlock()
        }
    }


}