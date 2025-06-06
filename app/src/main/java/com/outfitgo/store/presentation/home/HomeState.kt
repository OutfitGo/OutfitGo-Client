package com.outfitgo.store.presentation.home

import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.model.Coupon

data class HomeState(
    val isBrandsLoading: Boolean = true,
    val brandsLoadingError: String? = null,
    val brands: List<Collection> = emptyList<Collection>(),

    val isLatestProductsLoading: Boolean = false,
    val latestProductsLoadingError: String? = null,
    val latestProductsEndReached: Boolean = false,
    val latestProducts:List<Product> = emptyList<Product>()
    val coupons:List<Coupon> = emptyList()
)
