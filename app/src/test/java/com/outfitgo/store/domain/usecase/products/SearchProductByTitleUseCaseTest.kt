package com.outfitgo.store.domain.usecase.products

import com.outfitgo.store.data.repository.product.FakeProductsRepository
import com.outfitgo.store.domain.repository.product.ProductsRepository
import io.mockk.coEvery
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SearchProductByTitleUseCaseTest {
    lateinit var repo: ProductsRepository
    lateinit var useCase: SearchProductByTitleUseCase

    @Before
    fun setUp() {
        repo = FakeProductsRepository()
        useCase = SearchProductByTitleUseCase(repo)
    }

    @Test
    fun searchProductsByTitle_whenPassedEmptyTitle_returnsEmptyList() = runTest {
        val title = ""
        val result = useCase.execute(title)
        assertTrue(result.isEmpty())
    }

    @Test
    fun searchProductsByTitle_whenPassedCorrectTitle_returnsListOfProductsMatched() = runTest {
        val title = "Espresso Machine"

        val result = useCase.execute(title)
        val product = result.first()
        assertTrue(result.isNotEmpty())
        assertEquals("prod_006", product.id)

    }


}