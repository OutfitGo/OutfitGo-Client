package com.outfitgo.store.presentation.address

import com.outfitgo.store.domain.model.Address
import com.outfitgo.store.domain.usecase.address.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import kotlinx.coroutines.withTimeout
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.concurrent.atomic.AtomicReference

@OptIn(ExperimentalCoroutinesApi::class)
class AddressViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var getAddressesUseCase: GetUserAddressesUseCase
    private lateinit var getDefaultAddressUseCase: GetDefaultAddressUseCase
    private lateinit var deleteAddressUseCase: DeleteAddressUseCase
    private lateinit var addAddressUseCase: CreateAddressUseCase
    private lateinit var updateAddressUseCase: UpdateAddressUseCase
    private lateinit var setDefaultAddressUseCase: UpdateDefaultAddressUseCase

    private lateinit var viewModel: AddressViewModel

    @Before
    fun setup() {
        getAddressesUseCase = mockk()
        getDefaultAddressUseCase = mockk()
        deleteAddressUseCase = mockk()
        addAddressUseCase = mockk()
        updateAddressUseCase = mockk()
        setDefaultAddressUseCase = mockk()

        viewModel = AddressViewModel(
            getAddressesUseCase,
            getDefaultAddressUseCase,
            deleteAddressUseCase,
            addAddressUseCase,
            updateAddressUseCase,
            setDefaultAddressUseCase
        )
    }

    @Test
    fun `getCities intent should update state with city list`() = runTest {
        // Act
        viewModel.processIntent(AddressIntent.getCities)

        // Assert
        val state = viewModel.state.value
        assertThat(state.cities.isNotEmpty(), `is`(true))
        assertThat(state.cities.contains("Cairo"), `is`(true))
    }

    class MainCoroutineRule(
        private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    ) : TestWatcher() {
        override fun starting(description: Description) {
            Dispatchers.setMain(dispatcher)
        }

        override fun finished(description: Description) {
            Dispatchers.resetMain()
        }
    }
}
