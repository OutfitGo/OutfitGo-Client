package com.outfitgo.store.data.datasource.remote.wishlist

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.outfitgo.store.data.mappers.toProduct
import com.outfitgo.store.domain.model.product.Product
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "WishlistRemoteDataSourc"

class WishlistRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : WishlistRemoteDataSource {

    private fun cleanId(id: String): String {
        return id.split("/").last()
    }

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val WISHLIST_COLLECTION = "wishlist"
    }


    override suspend fun addProduct(userId: String, product: Product) {
        Log.d(TAG, "addProduct() called with: userId = $userId, product = $product")
        firestore.collection(USERS_COLLECTION)
            .document(cleanId(userId))
            .collection(WISHLIST_COLLECTION)
            .document(cleanId(product.id))
            .set(product)
            .await()
    }

    override suspend fun removeProduct(userId: String, productId: String) {
        firestore.collection(USERS_COLLECTION)
            .document(cleanId(userId))
            .collection(WISHLIST_COLLECTION)
            .document(cleanId(productId))
            .delete()
            .await()
    }

    override suspend fun getAllProducts(userId: String): List<Product> {
        return firestore.collection(USERS_COLLECTION)
            .document(cleanId(userId))
            .collection(WISHLIST_COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                // requires providing default values to Product primary constructor
                // document.toObject(Product::class.java)
                document.toProduct()
            }
    }

    override suspend fun isInWishlist(
        userId: String,
        productId: String
    ): Boolean {
        val products = firestore.collection(USERS_COLLECTION)
            .document(cleanId(userId))
            .collection(WISHLIST_COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                // requires providing default values to Product primary constructor
                // doc.toObject(Product::class.java)
                doc.toProduct()
            }
        return products.any { cleanId(it.id) == cleanId(productId) }
    }
}

