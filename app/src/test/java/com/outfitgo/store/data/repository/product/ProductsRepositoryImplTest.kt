package com.outfitgo.store.data.repository.product

import com.outfitgo.store.data.datasource.remote.product.ProductsRemoteDataSource
import com.outfitgo.store.domain.model.product.DetailedProduct
import com.outfitgo.store.domain.model.product.Product
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProductsRepositoryImplTest {


    private val mockProducts = listOf(
        Product(
            id = "prod_001",
            name = "Wireless Bluetooth Earbuds",
            type = "Electronics",
            price = "49.99",
            imageUrl = "https://example.com/images/earbuds.jpg",
            vendor = "SoundTech",
            pageCursor = "cursor_a1"
        ),
        Product(
            id = "prod_002",
            name = "Organic Cotton T-Shirt",
            type = "Apparel",
            price = "25.00",
            imageUrl = "https://example.com/images/tshirt.jpg",
            vendor = "EcoWear",
            pageCursor = "cursor_b2"
        ),
        Product(
            id = "prod_003",
            name = "Stainless Steel Water Bottle",
            type = "Home Goods",
            price = "19.95",
            imageUrl = "https://example.com/images/waterbottle.jpg",
            vendor = "HydratePro",
            pageCursor = "cursor_c3"
        ),
        Product(
            id = "prod_004",
            name = "Smart Home LED Bulb",
            type = "Electronics",
            price = "15.50",
            imageUrl = "https://example.com/images/ledbulb.jpg",
            vendor = "BrightConnect",
            pageCursor = "cursor_d4"
        ),
        Product(
            id = "prod_005",
            name = "Yoga Mat (Non-Slip)",
            type = "Fitness",
            price = "35.99",
            imageUrl = "https://example.com/images/yogamat.jpg",
            vendor = "FlexFit",
            pageCursor = "cursor_e5"
        ),
        Product(
            id = "prod_006",
            name = "Espresso Machine",
            type = "Kitchen Appliances",
            price = "199.00",
            imageUrl = "https://example.com/images/espresso.jpg",
            vendor = "BrewMaster",
            pageCursor = "cursor_f6"
        ),
        Product(
            id = "prod_007",
            name = "Noise-Cancelling Headphones",
            type = "Electronics",
            price = "129.99",
            imageUrl = "https://example.com/images/headphones.jpg",
            vendor = "AudioLux",
            pageCursor = "cursor_g7"
        ),
        Product(
            id = "prod_008",
            name = "Graphic Novel: The Future City",
            type = "Books",
            price = "18.00",
            imageUrl = "https://example.com/images/graphicnovel.jpg",
            vendor = "Storytellers Inc.",
            pageCursor = "cursor_h8"
        ),
        Product(
            id = "prod_009",
            name = "Travel Backpack (Waterproof)",
            type = "Bags",
            price = "75.00",
            imageUrl = "https://example.com/images/backpack.jpg",
            vendor = "ExploreGear",
            pageCursor = "cursor_i9"
        ),
        Product(
            id = "prod_010",
            name = "Artisan Scented Candle",
            type = "Home Decor",
            price = "12.75",
            imageUrl = "https://example.com/images/candle.jpg",
            vendor = "AromaCraft",
            pageCursor = "cursor_j10"
        )
    )
    lateinit var remoteDataSource: ProductsRemoteDataSource
    lateinit var repo: ProductsRepositoryImpl

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        repo = ProductsRepositoryImpl(remoteDataSource)
    }

    @Test
    fun fetchProductById_whenPassedCorrectId_ReturnsDetailedProductWithTheSameId() = runTest {
        // arrange
        val id = "prod_001"
        coEvery { remoteDataSource.fetchProductById(id) } answers {
            val product = mockProducts.find { it.id  == id }!!
            DetailedProduct(
                id = product.id,
                title = product.name,
                description = "",
                price = product.price,
                imageUrls = emptyList(),
                tags = emptyList(),
                vendor = product.vendor,
                category = "",
                rating = 5,
                reviews = emptyList(),
                currencyCode = "",
                variants = emptyList()
            )
        }

        // act
        val result = repo.fetchProductById(id)

        // assert
        assertEquals(id, result.id)
    }

    @Test
    fun fetchProductById_whenPassedWrongId_ThrowsException() = runTest {
        // arrange
        val id = "prod_000"
        coEvery { remoteDataSource.fetchProductById(id) } throws Exception("Invalid id")

        assertThrows(Exception::class.java) { runTest { repo.fetchProductById(id) } }

    }

    @Test
    fun searchProductsByTitle_whenPassedEmptyTitle_returnsEmptyList() = runTest {
        val title = ""
        coEvery { remoteDataSource.fetchProductsByTitle(title) } returns mockProducts.filter { it.name.isEmpty() }

        val result = repo.searchProductsByTitle(title)

        assertTrue(result.isEmpty())

    }

    @Test
    fun searchProductsByTitle_whenPassedCorrectTitle_returnsListOfProductsMatched() = runTest {
        val title = "Espresso Machine"
        coEvery { remoteDataSource.fetchProductsByTitle(title) } returns mockProducts.filter { it.name.contains(title) }

        val result = repo.searchProductsByTitle(title)
        val product = result.first()
        assertTrue(result.isNotEmpty())
        assertEquals("prod_006", product.id)

    }

}
