package com.outfitgo.store.domain.usecase.collections

import com.outfitgo.store.domain.repository.collections.CollectionsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.model.product.Product
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCategoriesUseCaseTest {

    private lateinit var fakeRepository: CollectionsRepository
    private lateinit var useCase: GetCategoriesUseCase

    private val allCollections = listOf(
        Collection(id = "1", name = "Men", handle = "men", imageUrl = "", pageCursor = ""),
        Collection(id = "2", name = "Women", handle = "women", imageUrl = "", pageCursor = ""),
        Collection(id = "3", name = "Kids", handle = "kids", imageUrl = "", pageCursor = ""),
        Collection(id = "4", name = "Sports", handle = "sports", imageUrl = "", pageCursor = ""),
        Collection(id = "5", name = "Accessories", handle = "accessories", imageUrl = "", pageCursor = ""),
    )

    @Before
    fun setup() {
        fakeRepository = object : CollectionsRepository {
            override suspend fun fetchCollections(): List<Collection> = allCollections

            override suspend fun fetchBrandCollectionProducts(
                brand: String,
                productName: String?,
                first: Int,
                after: String?
            ): List<Product> = emptyList()

            override suspend fun fetchCategoryCollectionProducts(categoryHandle: String): List<Product> = emptyList()
        }

        useCase = GetCategoriesUseCase(fakeRepository)
    }

    @Test
    fun execute_returnsLast4Collections() = runTest {
        val result = useCase.execute()

        assertEquals(4, result.size)
        assertEquals("2", result.first().id)
        assertEquals("5", result.last().id)
    }

    @Test
    fun execute_withLessThan4Collections_returnsAllAvailable() = runTest {
        val smallRepo = object : CollectionsRepository {
            override suspend fun fetchCollections(): List<Collection> =
                allCollections.take(2)

            override suspend fun fetchBrandCollectionProducts(
                brand: String,
                productName: String?,
                first: Int,
                after: String?
            ): List<Product> = emptyList()

            override suspend fun fetchCategoryCollectionProducts(categoryHandle: String): List<Product> = emptyList()
        }

        val smallUseCase = GetCategoriesUseCase(smallRepo)

        val result = smallUseCase.execute()

        assertEquals(2, result.size)
    }
}
