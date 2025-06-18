package com.outfitgo.store.domain.usecase.collections

import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.collections.CollectionsRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetBrandProductsUseCaseTest {

    private lateinit var fakeRepository: CollectionsRepository
    private lateinit var useCase: GetBrandProductsUseCase

    private val mockProducts = listOf(
        Product(id = "1", name = "Sneakers", vendor = "Nike", imageUrl = "", pageCursor = "", type = "men", price = "100"),
        Product(id = "2", name = "Shirt", vendor = "Nike", imageUrl = "", pageCursor = "", type = "men", price = "50"),
        Product(id = "3", name = "Shirt", vendor = "Adidas", imageUrl = "", pageCursor = "", type = "men", price = "55")
    )

    @Before
    fun setup() {
        fakeRepository = object : CollectionsRepository {
            override suspend fun fetchCollections(): List<Collection> = emptyList()

            override suspend fun fetchBrandCollectionProducts(
                brand: String,
                productName: String?,
                first: Int,
                after: String?
            ): List<Product> {
                return mockProducts.filter {
                    it.vendor.equals(brand, ignoreCase = true) &&
                            (productName?.isEmpty() != false || it.name.contains(productName.toString(), ignoreCase = true))
                }.take(first)
            }

            override suspend fun fetchCategoryCollectionProducts(categoryHandle: String): List<Product> = emptyList()
        }

        useCase = GetBrandProductsUseCase(fakeRepository)
    }

    @Test
    fun execute_withMatchingBrandAndProductName_returnsFilteredProducts() = runTest {
        val result = useCase.execute(brand = "Nike", productName = "Shirt", first = 5, after = null)

        assertEquals(1, result.size)
        assertEquals("Shirt", result.first().name)
        assertEquals("Nike", result.first().vendor)
    }

    @Test
    fun execute_withEmptyProductName_returnsAllBrandProductsUpToFirstLimit() = runTest {
        val result = useCase.execute(brand = "Nike", productName = "", first = 5, after = null)

        assertEquals(2, result.size)
        assertTrue(result.all { it.vendor == "Nike" })
    }

    @Test
    fun execute_withNonMatchingBrand_returnsEmptyList() = runTest {
        val result = useCase.execute(brand = "Puma", productName = "", first = 5, after = null)

        assertTrue(result.isEmpty())
    }
}
