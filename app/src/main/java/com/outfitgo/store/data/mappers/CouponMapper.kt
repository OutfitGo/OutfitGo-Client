package com.outfitgo.store.data.mappers

import com.outfitgo.store.admin.GetCouponsQuery
import com.outfitgo.store.domain.model.Coupon

fun GetCouponsQuery.Edge.toCoupons(): List<Coupon> {
    val discount = this.node.discount.onDiscountCodeBasic ?: return emptyList()
    return discount.codes.edges
        .map { it.node.code }
        .map { Coupon(code = it) }
}
