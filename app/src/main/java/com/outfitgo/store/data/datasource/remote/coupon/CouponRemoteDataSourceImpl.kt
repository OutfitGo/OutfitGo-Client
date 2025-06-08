package com.outfitgo.store.data.datasource.remote.coupon

import com.apollographql.apollo.ApolloClient
import com.outfitgo.store.admin.GetCouponsQuery
import com.outfitgo.store.data.mappers.toCoupons
import com.outfitgo.store.domain.model.Coupon
import javax.inject.Inject

class CouponRemoteDataSourceImpl @Inject constructor(
    private val remoteClient: ApolloClient
) : CouponRemoteDataSource {
    override suspend fun getCoupons(): List<Coupon> {
        val response = remoteClient.query(GetCouponsQuery()).execute()
        if (response.hasErrors()) {
            throw Exception(response.errors?.first()?.message)
        }

        val coupons = response.dataAssertNoErrors
            .discountNodes
            .edges
            .flatMap { it.toCoupons() }

        return coupons
    }
}