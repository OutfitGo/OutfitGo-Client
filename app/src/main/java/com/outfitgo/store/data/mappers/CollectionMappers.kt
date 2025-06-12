package com.outfitgo.store.data.mappers

import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.storefront.CollectionsQuery
import com.outfitgo.store.storefront.BrandCollectionProductsQuery
import com.outfitgo.store.storefront.CategoryCollectionProductsQuery

fun CollectionsQuery.Edge.toCollection(): Collection {
    return Collection(
        id = this.node.id,
        name = this.node.title,
        handle = this.node.handle,
        imageUrl = this.node.image?.url.toString(),
        pageCursor = this.cursor
    )
}

fun BrandCollectionProductsQuery.Edge.toProduct(): Product {
    return Product(
        id = node.id,
        name = node.title,
        type = node.productType,
        price = node.priceRange.minVariantPrice.amount as String,
        imageUrl = node.images.nodes.first().url.toString(),
        vendor = node.vendor,
        pageCursor = cursor
    )
}

fun CategoryCollectionProductsQuery.Edge.toProduct(): Product {
    return Product(
        id = node.id,
        name = node.title,
        type = node.productType,
        price = node.priceRange.minVariantPrice.amount as String,
        imageUrl = node.images.nodes.first().url.toString(),
        vendor = node.vendor,
        pageCursor = ""
    )
}
