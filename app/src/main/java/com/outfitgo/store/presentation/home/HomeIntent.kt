package com.outfitgo.store.presentation.home

import com.outfitgo.store.domain.model.product.CommonProduct

sealed interface HomeIntent {
    object GoToSearch : HomeIntent
    data class GoToBrandProducts(val brand: String) : HomeIntent
    object GetNextBrands : HomeIntent
    object GetNextLatestProducts : HomeIntent
    data class GoToProductDetails(val product: CommonProduct) : HomeIntent
    object GetCoupons:HomeIntent
}