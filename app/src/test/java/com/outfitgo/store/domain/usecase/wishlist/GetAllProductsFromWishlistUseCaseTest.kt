package com.outfitgo.store.domain.usecase.wishlist

import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.wishilst.WishlistRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetAllProductsFromWishlistUseCaseTest {

    lateinit var repo: WishlistRepository
    lateinit var mockWishlist: MutableList<Product>


    @Before
    fun setUp() {
        mockWishlist = mutableListOf(
            Product(
                id = "1",
                name = "Product-1",
                type = "Type-1",
                price = "1",
                imageUrl = "111",
                vendor = "Vendor-1",
                pageCursor = ""
            )
        )
        repo = mockk<WishlistRepository>()
    }

    @Test
    fun `execute should return all products from wishlist`() = runTest {
        // arrange
        val userId = "gid://Customer/12"
        coEvery { repo.getAllProducts(userId) } returns mockWishlist

        // act
        val useCase = GetAllProductsFromWishlistUseCase(repo)
        val result = useCase.execute(userId)

        // assert
        assertEquals(1, result.size)
    }

    fun `execute should return empty list if wishlist is empty`() = runTest{
        // arrange
        val userId = "gid://Customer/12"
        coEvery { repo.getAllProducts(userId) } returns emptyList()

        // act
        val useCase = GetAllProductsFromWishlistUseCase(repo)
        val result = useCase.execute(userId)

        // assert
        assertTrue(result.isEmpty())
    }

}