package com.outfitgo.store.data.datasource.remote.coupon

import com.outfitgo.store.domain.model.Coupon

interface CouponRemoteDataSource {
    suspend fun getCoupons():List<Coupon>
}