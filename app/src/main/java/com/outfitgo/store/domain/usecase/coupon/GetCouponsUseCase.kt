package com.outfitgo.store.domain.usecase.coupon

import com.outfitgo.store.domain.model.Coupon
import com.outfitgo.store.domain.repository.coupon.CouponsRepository
import javax.inject.Inject

class GetCouponsUseCase @Inject constructor(private val couponsRepository: CouponsRepository) {
    suspend fun execute():List<Coupon> = couponsRepository.getCoupons()
}