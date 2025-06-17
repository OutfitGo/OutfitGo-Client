package com.outfitgo.store.data.repository.wishlist

import com.outfitgo.store.data.datasource.remote.wishlist.FakeWishlistRemoteDataSource
import com.outfitgo.store.data.datasource.remote.wishlist.WishlistRemoteDataSource
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.wishilst.WishlistRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class WishlistRepositoryImplTest {

    private lateinit var fakeRemoteDataSource: FakeWishlistRemoteDataSource
    private lateinit var wishlistRepository: WishlistRepository

    private val testUserId = "user123"
    private val product1 = Product(
        id = "product_a",
        name = "Laptop",
        imageUrl = "url_a",
        type = "Type1",
        price = "11",
        vendor = "Vendor1",
        pageCursor = "",
    )
    private val product2 = Product(
        id = "product_b",
        name = "Laptop",
        imageUrl = "url_b",
        type = "Type2",
        price = "22",
        vendor = "Vendor2",
        pageCursor = "",
    )
    private val product3 = Product(
        id = "product_c",
        name = "Laptop",
        imageUrl = "url_c",
        type = "Type3",
        price = "33",
        vendor = "Vendor3",
        pageCursor = "",
    )


    @Before
    fun setUp() {
        fakeRemoteDataSource = FakeWishlistRemoteDataSource()
        wishlistRepository = WishlistRepositoryImpl(fakeRemoteDataSource)
    }


    @Before
    fun setup() {
        fakeRemoteDataSource = FakeWishlistRemoteDataSource()
        wishlistRepository = WishlistRepositoryImpl(fakeRemoteDataSource)
    }

    @Test
    fun `addProduct should add product to remote data source`() = runTest {
        assertEquals(0, fakeRemoteDataSource.getAllProducts(testUserId).size)
        wishlistRepository.addProduct(testUserId, product1)

        // Verify the product was added to the fake data source
        val currentWishlist = fakeRemoteDataSource.getAllProducts(testUserId)
        assertEquals(1, currentWishlist.size)
        assertTrue(currentWishlist.contains(product1))
    }

    @Test
    fun `addProduct should not add duplicate products based on id`() = runTest {
        // Set an initial state with product1 already in the wishlist
        fakeRemoteDataSource.setWishlistForUser(testUserId, listOf(product1))
        assertEquals(1, fakeRemoteDataSource.getAllProducts(testUserId).size)

        // Try to add the same product again (product1 has the same ID)
        wishlistRepository.addProduct(testUserId, product1)

        // Verify that the size remains 1 (no duplicate was added)
        val currentWishlist = fakeRemoteDataSource.getAllProducts(testUserId)
        assertEquals(1, currentWishlist.size)
        assertTrue(currentWishlist.contains(product1))
    }


    @Test
    fun `removeProduct should remove product from remote data source`() = runTest {
        // Set an initial state with product1 and product2 in the wishlist
        fakeRemoteDataSource.setWishlistForUser(testUserId, listOf(product1, product2))
        assertEquals(2, fakeRemoteDataSource.getAllProducts(testUserId).size)

        // Remove product1
        wishlistRepository.removeProduct(testUserId, product1.id)

        // Verify product1 is no longer in the wishlist, and product2 remains
        val currentWishlist = fakeRemoteDataSource.getAllProducts(testUserId)
        assertEquals(1, currentWishlist.size)
        assertFalse(currentWishlist.contains(product1))
        assertTrue(currentWishlist.contains(product2))
    }

    @Test
    fun `removeProduct should do nothing if product not found`() = runTest {
        // Set an initial state with product1
        fakeRemoteDataSource.setWishlistForUser(testUserId, listOf(product1))
        assertEquals(1, fakeRemoteDataSource.getAllProducts(testUserId).size)

        // Try to remove a product that doesn't exist
        wishlistRepository.removeProduct(testUserId, product2.id)

        // Verify the wishlist remains unchanged
        val currentWishlist = fakeRemoteDataSource.getAllProducts(testUserId)
        assertEquals(1, currentWishlist.size)
        assertTrue(currentWishlist.contains(product1))
    }

    @Test
    fun `getAllProducts should return all products from remote data source`() = runTest {
        // Set an initial state with multiple products
        val initialProducts = listOf(product1, product2, product3)
        fakeRemoteDataSource.setWishlistForUser(testUserId, initialProducts)

        // Get all products via the wishlistRepository
        val retrievedProducts = wishlistRepository.getAllProducts(testUserId)

        // Verify the retrieved list matches the initial state
        assertEquals(initialProducts.size, retrievedProducts.size)
        assertTrue(retrievedProducts.containsAll(initialProducts))
        assertTrue(initialProducts.containsAll(retrievedProducts)) // Ensure no extra products
    }

    @Test
    fun `getAllProducts should return empty list if wishlist is empty`() = runTest {
        // No products set initially for the user

        // Get all products via the wishlistRepository
        val retrievedProducts = wishlistRepository.getAllProducts(testUserId)

        // Verify the list is empty
        assertTrue(retrievedProducts.isEmpty())
    }

    @Test
    fun `isInWishlist should return true if product is in wishlist`() = runTest {
        // Set an initial state with product1 in the wishlist
        fakeRemoteDataSource.setWishlistForUser(testUserId, listOf(product1))

        // Check if product1 is in wishlist
        val isIn = wishlistRepository.isInWishlist(testUserId, product1.id)

        // Verify it returns true
        assertTrue(isIn)
    }

    @Test
    fun `isInWishlist should return false if product is not in wishlist`() = runTest {
        // Set an initial state with product1 in the wishlist
        fakeRemoteDataSource.setWishlistForUser(testUserId, listOf(product1))

        // Check if product2 (which is not in the initial state) is in wishlist
        val isIn = wishlistRepository.isInWishlist(testUserId, product2.id)

        // Verify it returns false
        assertFalse(isIn)
    }

    @Test
    fun `isInWishlist should return false if wishlist is empty`() = runTest {
        // Wishlist is empty by default after setup()

        // Check for any product
        val isIn = wishlistRepository.isInWishlist(testUserId, product1.id)

        // Verify it returns false
        assertFalse(isIn)
    }



}