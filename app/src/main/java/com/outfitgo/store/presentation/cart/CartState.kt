package com.outfitgo.store.presentation.cart

import com.outfitgo.store.domain.model.cart.CartItem
import com.outfitgo.store.domain.model.cart.Cost

data class CartState(
    val cartItems: List<CartItem> = emptyList(),
    val checkoutUrl: String="",
    val isLoading: Boolean = false,
    val coupon: String = "",
    val couponMessage: String? = null,
    val isCouponApplied: Boolean = false,
    val cartCost: Cost = Cost("0.0","0.0","0.0"),
    val error:String? = null
)