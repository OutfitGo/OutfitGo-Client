package com.outfitgo.store.presentation.home

import com.outfitgo.store.domain.model.product.Product

sealed interface HomeIntent {
    object GoToSearch : HomeIntent
    object GoToSettings : HomeIntent
    data class GoToBrandProducts(val brand: String) : HomeIntent
    object GetNextBrands : HomeIntent
    object GetNextLatestProducts : HomeIntent
    data class GoToProductDetails(val product: Product) : HomeIntent
    object GetCoupons:HomeIntent
}