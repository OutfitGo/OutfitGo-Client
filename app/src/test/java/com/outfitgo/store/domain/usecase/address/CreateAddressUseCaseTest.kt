package com.outfitgo.store.domain.usecase.address

import com.outfitgo.store.core.util.exceptions.MissingUserTokenException
import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.domain.repository.address.AddressRepository
import com.outfitgo.store.domain.repository.user.UsersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class CreateAddressUseCaseTest {

    private lateinit var mockUsersRepo: UsersRepository
    private lateinit var mockAddressRepo: AddressRepository
    private lateinit var useCase: CreateAddressUseCase

    private val sampleAddress = Address(
        id = "1",
        firstName = "Ali",
        lastName = "Ibrahim",
        line = "123 Main St",
        city = "Cairo",
        isDefault = false
    )

    @Before
    fun setup() {
        mockUsersRepo = mockk()
        mockAddressRepo = mockk()
        useCase = CreateAddressUseCase(mockAddressRepo, mockUsersRepo)
    }

    @Test
    fun `invoke with valid token should call createAddress`() = runTest {
        coEvery { mockUsersRepo.getSavedUserToken() } returns "valid_token"
        coEvery { mockAddressRepo.createAddress("valid_token", sampleAddress) } returns Unit

        useCase.invoke(sampleAddress)

        coVerify(exactly = 1) {
            mockAddressRepo.createAddress("valid_token", sampleAddress)
        }
    }

    @Test
    fun `invoke with null token should throw MissingUserTokenException`() = runTest {
        coEvery { mockUsersRepo.getSavedUserToken() } returns null

        assertFailsWith<MissingUserTokenException> {
            useCase.invoke(sampleAddress)
        }

        coVerify(exactly = 0) {
            mockAddressRepo.createAddress(any(), any())
        }
    }
}
