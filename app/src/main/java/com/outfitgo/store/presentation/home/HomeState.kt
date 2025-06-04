package com.outfitgo.store.presentation.home

import com.outfitgo.store.domain.model.Coupon
import com.outfitgo.store.domain.model.brand.Brand
import com.outfitgo.store.domain.model.product.CommonProduct

data class HomeState(
    val isBrandsLoading: Boolean = false,
    val brandsLoadingError: String? = null,
    val brandEndReached: Boolean = false,
    val brands: List<Brand> = emptyList<Brand>(),

    val isLatestProductsLoading: Boolean = false,
    val latestProductsLoadingError: String? = null,
    val latestProductsEndReached: Boolean = false,
    val latestProducts:List<CommonProduct> = emptyList<CommonProduct>(),

    val coupons:List<Coupon> = emptyList()
)
