package com.outfitgo.store.domain.usecase.wishlist

import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.wishilst.WishlistRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AddProductToWishlistUseCaseTest {

    lateinit var repo: WishlistRepository
    lateinit var mockWishlist: MutableList<Product>
    lateinit var mockProduct: Product

    @Before
    fun setUp() {
        mockProduct = Product(
            id = "1",
            name = "Product-1",
            type = "Type-1",
            price = "1",
            imageUrl = "111",
            vendor = "Vendor-1",
            pageCursor = ""
        )
        mockWishlist = mutableListOf()
        repo = mockk<WishlistRepository>()
    }

    @Test
    fun `execute should add product to wishlist`() = runTest {
        // arrange
        val userId = "gid://Customer/12"
        coEvery { repo.addProduct(userId, mockProduct) } returns Unit

        // act
        val useCase = AddProductToWishlistUseCase(repo)
        useCase.execute(userId, mockProduct)

        // assert

    }
}