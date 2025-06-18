package com.outfitgo.store.data.repository.address

import com.outfitgo.store.data.datasource.remote.address.FakeAddressRemoteDataSource
import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.domain.repository.address.AddressRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class AddressRepositoryTest {

    private lateinit var fakeRemoteDataSource: FakeAddressRemoteDataSource
    private lateinit var repository: AddressRepository

    private val address1 = Address(
        id = "1",
        firstName = "Ali",
        lastName = "Ibrahim",
        line = "123 Main Street",
        city = "Cairo",
        isDefault = false
    )

    private val address2 = Address(
        id = "2",
        firstName = "Sara",
        lastName = "Ahmed",
        line = "456 Nile Avenue",
        city = "Giza",
        isDefault = true
    )

    @Before
    fun setup() {
        fakeRemoteDataSource = FakeAddressRemoteDataSource(
            addresses = mutableListOf(address1, address2),
            defaultAddressId = address2.id
        )
        repository = AddressRepositoryImpl(fakeRemoteDataSource)
    }

    @Test
    fun getAddresses_returnsAllAddresses() = runTest {
        val result = repository.getAddresses("fake_token")
        assertEquals(2, result.size)
        assertTrue(result.any { it.firstName == "Ali" })
        assertTrue(result.any { it.firstName == "Sara" })
    }

    @Test
    fun getDefaultAddress_returnsCorrectAddress() = runTest {
        val result = repository.getDefaultAddress("fake_token")
        assertEquals("2", result.id)
        assertEquals("Sara", result.firstName)
    }

    @Test
    fun createAddress_addsNewAddress() = runTest {
        val newAddress = Address(
            id = "3",
            firstName = "Ziad",
            lastName = "Helaly",
            line = "789 Palm Road",
            city = "Alexandria",
            isDefault = false
        )

        repository.createAddress("fake_token", newAddress)
        val all = repository.getAddresses("fake_token")
        assertEquals(3, all.size)
        assertTrue(all.any { it.id == "3" && it.firstName == "Ziad" })
    }

    @Test
    fun updateAddress_modifiesExistingAddress() = runTest {
        val updatedAddress = address1.copy(firstName = "Ali Updated")
        repository.updateAddress("fake_token", updatedAddress)

        val result = repository.getAddresses("fake_token").first { it.id == address1.id }
        assertEquals("Ali Updated", result.firstName)
    }

    @Test
    fun deleteAddress_removesAddress() = runTest {
        repository.deleteAddress("fake_token", address1.id)
        val all = repository.getAddresses("fake_token")
        assertEquals(1, all.size)
        assertFalse(all.any { it.id == address1.id })
    }

    @Test
    fun setDefaultAddress_updatesDefaultCorrectly() = runTest {
        repository.setDefaultAddress("fake_token", address1.id)
        val default = repository.getDefaultAddress("fake_token")
        assertEquals(address1.id, default.id)
    }
}
