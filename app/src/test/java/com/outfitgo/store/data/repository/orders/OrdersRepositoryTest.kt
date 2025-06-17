package com.outfitgo.store.data.repository.orders

import com.outfitgo.store.data.datasource.remote.orders.FakeOrdersRemoteDataSource
import com.outfitgo.store.domain.model.FinancialStatus
import com.outfitgo.store.domain.model.cart.CartItem
import com.outfitgo.store.domain.model.cart.Merchandise
import com.outfitgo.store.domain.model.order.Order
import com.outfitgo.store.domain.model.order.OrderContactInfo
import com.outfitgo.store.domain.model.order.OrderProduct
import com.outfitgo.store.domain.model.order.OrderShippingAddress
import com.outfitgo.store.domain.model.order.OrdersResponse
import com.outfitgo.store.domain.repository.orders.OrdersRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class OrdersRepositoryTest {

    private lateinit var fakeRemoteDataSource: FakeOrdersRemoteDataSource
    private lateinit var repository: OrdersRepository

    private val dummyOrdersResponse = OrdersResponse(
        ordersCount = 1,
        orders = listOf(
            Order(
                id = "1",
                number = 123,
                date = "2025-06-12",
                paymentStatus = "Paid",
                totalPrice = "150.00",
                items = listOf(
                    OrderProduct(
                        id = "1",
                        name = "Product 1",
                        price = "100",
                        imageUrl = "",
                        quantity = 10,
                        variantTitle = "small"
                    )
                ),
                contactInfo = OrderContactInfo(name = "Mahmoud", email = "mahmoud@example.com"),
                trackingUrl = "https://tracking.com/1",
                shippingAddress = OrderShippingAddress("Street", "City", "Country", "12345"),
                itemsCount = 1,
                pageCursor = "cursor1"
            )
        )
    )

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeOrdersRemoteDataSource(
            ordersResponse = dummyOrdersResponse,
            createOrderSuccess = true
        )
        repository = OrdersRepositoryImpl(fakeRemoteDataSource)
    }

    @Test
    fun getCustomerOrders_returnsCorrectOrdersResponse() = runTest {
        val result = repository.getCustomerOrders(
            customerToken = "fake_token",
            first = 10,
            after = null
        )

        assertNotNull(result)
        assertEquals(1, result.ordersCount)
        assertEquals("1", result.orders.first().id)
        assertEquals("Mahmoud", result.orders.first().contactInfo.name)
    }

    @Test
    fun createOrder_returnsTrueWhenSuccessful() = runTest {
        val result = repository.createOrder(
            customerEmail = "mahmoud@gmail.com",
            financialStatus = FinancialStatus.PAID,
            shippingAddress = OrderShippingAddress("Street", "City", "Country", "12345"),
            cartItems = listOf(
                CartItem(
                    id = "1",
                    quantity = 10,
                    merchandise = Merchandise(title = "", price = "", img = "", variantId = ""),
                )
            )
        )

        assertTrue(result)
    }
}
