package com.outfitgo.store.presentation.orderdetails

sealed interface OrderDetailsIntent {
    object GoBack : OrderDetailsIntent
    data class NavigateToProductDetails(val productId: String) : OrderDetailsIntent
}