package com.outfitgo.store.data.mappers

import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.storefront.LatestProductsQuery

fun LatestProductsQuery.Edge.toCommonProduct(): CommonProduct {
    return CommonProduct(
        id = node.id,
        name = node.title,
        type = node.productType,
        price = node.priceRange.minVariantPrice.amount as String,
        imageUrl = node.images.nodes.first().url.toString(),
        vendor = node.vendor,
        pageCursor = cursor
    )
}