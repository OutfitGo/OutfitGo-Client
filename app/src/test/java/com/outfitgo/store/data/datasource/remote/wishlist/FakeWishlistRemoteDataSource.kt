package com.outfitgo.store.data.datasource.remote.wishlist

import com.outfitgo.store.domain.model.product.Product


class FakeWishlistRemoteDataSource(
    val map: MutableMap<String, MutableList<Product>> = mutableMapOf()
): WishlistRemoteDataSource {

    private fun cleanId(id: String): String {
        return id.split("/").last()
    }

    override suspend fun addProduct(
        userId: String,
        product: Product
    ) {
        val wishlist = map.getOrPut(cleanId(userId)) { mutableListOf() }
        // Only add if the product with this ID is not already present
        if (wishlist.none { it.id == product.id }) {
            wishlist.add(product)
        }
    }

    override suspend fun removeProduct(userId: String, productId: String) {
        val wishlist = map.getOrPut(cleanId(userId)) { mutableListOf() }
        wishlist.removeIf { it.id  == productId }
    }

    override suspend fun getAllProducts(userId: String): List<Product> {
        return map.getOrElse(cleanId(userId)) { emptyList() }
    }

    override suspend fun isInWishlist(
        userId: String,
        productId: String
    ): Boolean {
        val wishlist = map.getOrElse(cleanId(userId)) { emptyList() }
        return productId in wishlist.map { it.id }
    }

    fun setWishlistForUser(userId: String, products: List<Product>) {
        map[cleanId(userId)] = products.toMutableList()
    }
}