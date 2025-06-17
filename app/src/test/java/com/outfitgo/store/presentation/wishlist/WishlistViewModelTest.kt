package com.outfitgo.store.presentation.wishlist

import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.usecase.auth.GetSavedUserIdUseCase
import com.outfitgo.store.domain.usecase.wishlist.GetAllProductsFromWishlistUseCase
import com.outfitgo.store.domain.usecase.wishlist.RemoveProductFromWishlistUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class WishlistViewModelTest {

    val mockWishlist = listOf<Product>(
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

    private val getAllProductsFromWishlistUseCase: GetAllProductsFromWishlistUseCase = mockk()
    private val removeProductFromWishlistUseCase: RemoveProductFromWishlistUseCase = mockk()
    private val getSavedUserIdUseCase: GetSavedUserIdUseCase = mockk()
    lateinit var viewModel: WishlistViewModel

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getSavedUserIdUseCase.execute() } returns "gid://Customer/12"
        viewModel = WishlistViewModel(
            getAllProductsFromWishlistUseCase,
            removeProductFromWishlistUseCase,
            getSavedUserIdUseCase
        )
    }


    @Test
    fun `init block should load userId and then products`() = runTest {

        val expectedProducts = listOf(Product("1", "Product A", "", "12.2", "", "Nike", ""))
        coEvery { getAllProductsFromWishlistUseCase.execute("gid://Customer/12") } returns expectedProducts

        // Re-initialize ViewModel after mocking getSavedUserIdUseCase for this specific test
        viewModel = WishlistViewModel(
            getAllProductsFromWishlistUseCase,
            removeProductFromWishlistUseCase,
            getSavedUserIdUseCase
        )

        // Advance the dispatcher to allow init block coroutine to complete
        advanceUntilIdle()

        assertEquals(viewModel.userId, "gid://Customer/12")
        assertEquals(viewModel.state.value.products, expectedProducts)
        assertFalse(viewModel.state.value.isLoading)
    }



    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


}