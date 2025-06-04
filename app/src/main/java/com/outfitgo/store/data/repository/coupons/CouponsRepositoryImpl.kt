package com.outfitgo.store.data.repository.coupons

import com.outfitgo.store.data.datasource.remote.coupon.CouponRemoteDataSource
import com.outfitgo.store.domain.model.Coupon
import com.outfitgo.store.domain.repository.coupon.CouponsRepository
import javax.inject.Inject

class CouponsRepositoryImpl @Inject constructor(private val remoteDataSource: CouponRemoteDataSource) :
    CouponsRepository {
    override suspend fun getCoupons(): List<Coupon> {
        return remoteDataSource.getCoupons()
    }
}