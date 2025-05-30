package com.outfitgo.store.presentation.home

sealed interface HomeIntent {
    object GoToSearch: HomeIntent
    object GoToBrandProducts: HomeIntent
    object GetNextBrands: HomeIntent
    object GetNextLatestProducts: HomeIntent
    object GoToProductDetails: HomeIntent
}