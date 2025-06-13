package com.outfitgo.store.data.mappers

import com.google.firebase.firestore.DocumentSnapshot
import com.outfitgo.store.domain.model.product.DetailedProduct
import com.outfitgo.store.domain.model.ReviewUtils
import com.outfitgo.store.domain.model.product.OrderProduct
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.storefront.CustomerOrdersQuery
import com.outfitgo.store.domain.model.product.ProductVariant
import com.outfitgo.store.storefront.GetProductByIdQuery
import com.outfitgo.store.storefront.LatestProductsQuery

fun LatestProductsQuery.Edge.toProduct(): Product {
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

fun GetProductByIdQuery.Product.toDetailedProduct(): DetailedProduct {
    return DetailedProduct(
        id = this.id,
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
        variants = this.variants.nodes.map { ProductVariant(id = it.id, title = it.title) }
    )

}

fun DetailedProduct.toProduct(): Product {
    return Product(
        id = this.id,
        name = this.title,
        type = this.category,
        price = this.price,
        imageUrl = this.imageUrls.first(),
        vendor = this.vendor,
        pageCursor = ""
    )
}

fun DocumentSnapshot.toProduct(): Product? {
    return try {
        Product(
            id = this.getString("id") ?: throw IllegalArgumentException("Product 'id' cannot be null"),
            name = this.getString("name") ?: throw IllegalArgumentException("Product 'name' cannot be null"),
            type = this.getString("type") ?: throw IllegalArgumentException("Product 'type' cannot be null"),
            price = this.getString("price") ?: throw IllegalArgumentException("Product 'price' cannot be null"),
            imageUrl = this.getString("imageUrl") ?: throw IllegalArgumentException("Product 'imageUrl' cannot be null"),
            vendor = this.getString("vendor") ?: throw IllegalArgumentException("Product 'vendor' cannot be null"),
            pageCursor = this.getString("pageCursor") ?: throw IllegalArgumentException("Product 'pageCursor' cannot be null")
        )
    } catch (e: Exception) {
        println("Error deserializing product: ${e.message} for document ${this.id}")
        null
    }
}

fun CustomerOrdersQuery.Edge1.toOrderProduct(): OrderProduct{
    return OrderProduct(
        id = node.variant?.product?.id ?: throw IllegalArgumentException("Product 'id' cannot be null"),
        name = node.title,
        price = node.variant.product.priceRange.minVariantPrice.amount as String,
        imageUrl = node.variant.product.images.nodes.first().url.toString(),
        quantity = node.quantity,
        variantTitle = node.variant.title
    )
}
