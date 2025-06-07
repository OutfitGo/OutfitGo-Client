package com.outfitgo.store.presentation.cart

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.outfitgo.store.core.util.Const
import com.outfitgo.store.domain.model.cart.Cost
import com.outfitgo.store.domain.usecase.cart.ApplyCouponToCartUseCase
import com.outfitgo.store.domain.usecase.cart.GetCartUseCase
import com.outfitgo.store.domain.usecase.cart.RemoveItemFromCartUseCase
import com.outfitgo.store.domain.usecase.cart.UpdateCartLineQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val applyCouponToCartUseCase: ApplyCouponToCartUseCase,
    private val removeItemFromCartUseCase: RemoveItemFromCartUseCase,
    private val updateCartLineQuantityUseCase: UpdateCartLineQuantityUseCase
) : ViewModel() {
    private val _cartState: MutableStateFlow<CartState> = MutableStateFlow(CartState())
    val cartState = _cartState.asStateFlow()
    val cartId = "gid://shopify/Cart/Z2NwLWV1cm9wZS13ZXN0MTowMUpXWUFYWTQ0UDFSWFFFMlJCNFAxSzdLRA?key=c8eefa970b2177b0b16a4e3838320891"
    private val _effect = MutableSharedFlow<CartEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _cartState.update { it.copy(isLoading = true) }
                val response = getCartUseCase.execute(cartId)
                _cartState.update {
                    it.copy(
                        cartItems = response.items ?: emptyList(),
                        coupon = response.discountCode?.code ?: "",
                        isCouponApplied = response.discountCode?.applicable ?: false,
                        couponMessage = if (response.discountCode?.applicable == true) {
                            "Coupon Applied"
                        } else {
                            "Invalid Coupon"
                        },
                        cartCost = response.cost ?: Cost("0.0"),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _cartState.update {
                    it.copy(
                        error = "${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun processIntent(intent: CartIntent) {
        when (intent) {
            is CartIntent.ApplyCoupon -> applyCoupon()
            is CartIntent.DecreaseItemQuantity -> decreaseItemQuantity(intent.id, intent.quantity)
            is CartIntent.IncreaseItemQuantity -> increaseItemQuantity(intent.id, intent.quantity)
            is CartIntent.RemoveItem -> removeItem(intent.id)
            is CartIntent.UpdateCouponCode -> updateCoupon(intent.code)
        }
    }

    private fun increaseItemQuantity(lineId: String, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            updateCurrentLineQuantity(lineId, quantity + 1)
            val newCost =
                updateCartLineQuantityUseCase.execute(cartId = cartId, lineId, quantity + 1)
            _cartState.update {
                it.copy(cartCost = newCost)
            }
        }
    }

    private fun decreaseItemQuantity(lineId: String, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (quantity == 1) {
                _effect.emit(CartEffect.ShowRemoveItemWarning(lineId))
            } else {
                updateCurrentLineQuantity(lineId, quantity - 1)
                val newCost =
                    updateCartLineQuantityUseCase.execute(cartId = cartId, lineId, quantity - 1)
                _cartState.update {
                    it.copy(cartCost = newCost)
                }
            }
        }
    }

    private fun updateCurrentLineQuantity(lineId: String, quantity: Int) {
        _cartState.update { currentState ->
            val updatedItems = currentState.cartItems.map { item ->
                if (item.id == lineId) {
                    item.copy(quantity = quantity)
                } else {
                    item
                }
            }
            currentState.copy(cartItems = updatedItems)
        }
    }

    private fun updateCoupon(newCoupon: String) {
        _cartState.update { it.copy(coupon = newCoupon) }
    }

    private fun applyCoupon() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = applyCouponToCartUseCase.execute(cartId, _cartState.value.coupon)
            if (response.discountCode?.applicable == true) {
                _cartState.update {
                    it.copy(
                        couponMessage = "Coupon Applied",
                        isCouponApplied = true,
                        cartCost = response.cost ?: Cost("0.0")
                    )
                }
            } else {
                _cartState.update {
                    it.copy(
                        couponMessage = "Invalid Coupon",
                        isCouponApplied = false
                    )
                }
            }
        }
    }

    private fun removeItem(lineId: String){
        viewModelScope.launch(Dispatchers.IO){
            val response = removeItemFromCartUseCase.execute(cartId, lineId)
            val updatedItems = _cartState.value.cartItems.filter { it.id != lineId }
            _cartState.update {
                it.copy(
                    cartItems = updatedItems,
                    cartCost = response
                )
            }
        }
    }

}