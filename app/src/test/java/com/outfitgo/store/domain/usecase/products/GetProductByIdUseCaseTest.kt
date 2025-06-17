package com.outfitgo.store.domain.usecase.products

import com.outfitgo.store.data.repository.product.FakeProductsRepository
import com.outfitgo.store.domain.repository.product.ProductsRepository
import kotlinx.coroutines.test.runTest
import okhttp3.internal.userAgent
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetProductByIdUseCaseTest {
    lateinit var repo: ProductsRepository
    lateinit var useCase: GetProductByIdUseCase

    @Before
    fun setUp() {
        repo = FakeProductsRepository()
        useCase = GetProductByIdUseCase(repo)
    }

    @Test
    fun execute_WhenPassedCorrectId_ReturnsDetailedProduct() = runTest {
        val id = "prod_001"
        val product = useCase.execute(id)
        assertEquals(id, product.id)
    }

    @Test
    fun execute_WhenPassedWrongId_ReturnsDetailedProduct() = runTest {
        val id = "prod_001112"
        assertThrows(Exception::class.java) {
            runTest { useCase.execute(id) }
        }
    }


}