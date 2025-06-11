package com.outfitgo.store.domain.repository.coupon

import com.outfitgo.store.domain.model.Coupon

interface CouponsRepository {
    suspend fun getCoupons():List<Coupon>
}