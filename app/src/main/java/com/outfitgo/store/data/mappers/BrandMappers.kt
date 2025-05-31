package com.outfitgo.store.data.mappers

import com.outfitgo.store.domain.model.brand.Brand
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.storefront.BrandProductsQuery
import com.outfitgo.store.storefront.BrandsQuery

fun BrandsQuery.Edge.toBrand(): Brand {
    return Brand(
        id = this.node.id,
        name = this.node.title,
        imageUrl = this.node.image?.url.toString(),
        pageCursor = this.cursor
    )
}

fun BrandProductsQuery.Edge.toCommonProduct(): CommonProduct {
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