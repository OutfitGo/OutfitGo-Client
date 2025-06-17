package com.outfitgo.store.data.repository.collections

import com.outfitgo.store.data.datasource.remote.collections.FakeCollectionsRemoteDataSource
import com.outfitgo.store.domain.model.product.Product
import com.outfitgo.store.domain.repository.collections.CollectionsRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import com.outfitgo.store.domain.model.Collection
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CollectionsRepositoryTest {

    private lateinit var fakeRemoteDataSource: FakeCollectionsRemoteDataSource
    private lateinit var repository: CollectionsRepository

    private val mockCollections = listOf(
        Collection(id = "1", name = "Men", handle = "men", imageUrl = "", pageCursor = ""),
        Collection(id = "2", name = "Women", handle = "women", imageUrl = "", pageCursor = ""),
        Collection(id = "3", name = "Kids", handle = "kids", imageUrl = "", pageCursor = ""),
    )

    private val mockProducts = listOf(
        Product(
            id = "1",
            name = "Product 1",
            vendor = "Nike",
            imageUrl = "",
            pageCursor = "",
            type = "men",
            price = ""
        ),
        Product(
            id = "2",
            name = "Product 2",
            vendor = "Nike",
            imageUrl = "",
            pageCursor = "",
            type = "women",
            price = ""
        ),
        Product(
            id = "3",
            name = "Product 3",
            vendor = "Vans",
            imageUrl = "",
            pageCursor = "",
            type = "men",
            price = ""
        ),
    )

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeCollectionsRemoteDataSource(
            collections = mockCollections,
            products = mockProducts
        )
        repository = CollectionsRepositoryImpl(fakeRemoteDataSource)
    }

    @Test
    fun fetchCollections_returnsAllCollections() = runTest {
        val result = repository.fetchCollections()
        assertEquals(mockCollections.size, result.size)
        assertEquals(mockCollections.first(), result.first())
    }

    @Test
    fun fetchBrandCollectionProducts_withBrandFilter_returnsFilteredProducts() = runTest {
        val result = repository.fetchBrandCollectionProducts(
            brand = "Nike",
            productName = null,
            first = 10,
            after = null
        )
        assertEquals(2, result.size)
        assertTrue(result.all { it.vendor == "Nike" })
    }

    @Test
    fun fetchCategoryCollectionProducts_withCategoryHandle_returnsMatchingProducts() = runTest {
        val result = repository.fetchCategoryCollectionProducts("men")
        assertEquals(2, result.size)
        assertTrue(result.all { it.type == "men" })
    }
}
