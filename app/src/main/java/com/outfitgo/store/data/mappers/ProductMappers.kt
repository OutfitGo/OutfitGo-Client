package com.outfitgo.store.data.mappers

import com.outfitgo.store.domain.model.product.DetailedProduct
import com.outfitgo.store.domain.model.ReviewUtils
import com.outfitgo.store.domain.model.product.CommonProduct
import com.outfitgo.store.storefront.GetProductByIdQuery
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

fun GetProductByIdQuery.Product.toDetailedProduct(): DetailedProduct {



    return DetailedProduct(
        id = this.variants.edges.first().node.id,
        title = this.title,
        description = this.description,
        price = "${this.priceRange.maxVariantPrice.amount}",
        currencyCode = "${this.priceRange.maxVariantPrice.currencyCode}",
        rating = ReviewUtils.generateRandomRating(),
        tags = this.tags,
        vendor = this.vendor,
        category = this.productType,
        imageUrls = this.images.nodes.map { "${it.src}" },
        reviews = ReviewUtils.generateRandomReviews(),
    )
}