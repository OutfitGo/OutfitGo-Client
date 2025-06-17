package com.outfitgo.store.domain.usecase.orders

import com.outfitgo.store.core.util.exceptions.MissingUserTokenException
import com.outfitgo.store.domain.model.order.Order
import com.outfitgo.store.domain.model.order.OrderContactInfo
import com.outfitgo.store.domain.model.order.OrdersResponse
import com.outfitgo.store.domain.repository.orders.OrdersRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class GetCustomerOrdersUseCaseTest {

    lateinit var mockUsersRepo: UsersRepository
    lateinit var mockOrdersRepo: OrdersRepository

    @Before
    fun setup(){
        mockUsersRepo = mockk<UsersRepository>()
        mockOrdersRepo = mockk<OrdersRepository>()
    }

    private val mockOrders = listOf(
        Order(
            id = "1",
            number = 1001,
            date = "2025-06-12",
            paymentStatus = "PAID",
            totalPrice = "150.00",
            items = emptyList(),
            contactInfo = OrderContactInfo("Mahmoud", "0123456789"),
            trackingUrl = "",
            shippingAddress = null,
            itemsCount = 2,
            pageCursor = ""
        )
    )

    private val mockOrdersResponse = OrdersResponse(
        ordersCount = 1,
        orders = mockOrders
    )

    @Test
    fun execute_withValidTokenAndOrders_returnsOrdersResponse() = runTest {

        coEvery { mockUsersRepo.getSavedUserToken() } returns "valid_token"
        coEvery {
            mockOrdersRepo.getCustomerOrders("valid_token", any(), any())
        } returns mockOrdersResponse

        val useCase = GetCustomerOrdersUseCase(mockOrdersRepo, mockUsersRepo)

        val result = useCase.execute(first = 10, after = null)

        assertEquals(1, result.ordersCount)
    }

    @Test
    fun execute_withNullToken_throwsMissingUserTokenException() = runTest {
        val mockUsersRepository = mockk<UsersRepository>()
        val mockOrdersRepository = mockk<OrdersRepository>()

        coEvery { mockUsersRepository.getSavedUserToken() } returns null

        val useCase = GetCustomerOrdersUseCase(mockOrdersRepository, mockUsersRepository)

        assertFailsWith<MissingUserTokenException> {
            useCase.execute(10, null)
        }
    }


    @Test
    fun execute_withNullOrdersResponse_throwsException() = runTest {
        val mockUsersRepository = mockk<UsersRepository>()
        val mockOrdersRepository = mockk<OrdersRepository>()

        coEvery { mockUsersRepository.getSavedUserToken() } returns "valid_token"
        coEvery {
            mockOrdersRepository.getCustomerOrders(
                customerToken = "valid_token",
                first = 10,
                after = null
            )
        } returns null

        val useCase = GetCustomerOrdersUseCase(mockOrdersRepository, mockUsersRepository)

        val exception = assertFailsWith<Exception> {
            useCase.execute(10, null)
        }

        assertEquals("Can't fetch your orders, try again later", exception.message)
    }

}
