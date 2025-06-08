package com.outfitgo.store.presentation.cart

sealed interface CartIntent {
    data class IncreaseItemQuantity(val id: String, val quantity: Int) : CartIntent
    data class DecreaseItemQuantity(val id: String, val quantity: Int) : CartIntent
    data class RemoveItem(val id: String) : CartIntent
    data class UpdateCouponCode(val code: String) : CartIntent
    object ApplyCoupon : CartIntent
}