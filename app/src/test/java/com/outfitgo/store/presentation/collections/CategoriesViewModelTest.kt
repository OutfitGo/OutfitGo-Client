package com.outfitgo.store.presentation.collections

import com.outfitgo.store.domain.model.Collection
import com.outfitgo.store.domain.usecase.collections.GetCategoriesUseCase
import com.outfitgo.store.presentation.categories.CategoriesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var getCategoriesUseCase: GetCategoriesUseCase
    private lateinit var viewModel: CategoriesViewModel

    private val mockCategories = listOf(
        Collection(id = "1", name = "Men", handle = "men", imageUrl = "", pageCursor = ""),
        Collection(id = "2", name = "Women", handle = "women", imageUrl = "", pageCursor = ""),
        Collection(id = "3", name = "Kids", handle = "kids", imageUrl = "", pageCursor = ""),
        Collection(id = "4", name = "Accessories", handle = "accessories", imageUrl = "", pageCursor = "")
    )

    @Before
    fun setup() {
        getCategoriesUseCase = mockk()
        coEvery { getCategoriesUseCase.execute() } returns mockCategories
        viewModel = CategoriesViewModel(getCategoriesUseCase)
    }

    @Test
    fun `getCategories should update uiState with categories`() = runTest {
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertThat(state.isLoading, `is`(false))
        assertThat(state.categories, `is`(mockCategories))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    class MainCoroutineRule(
        private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    ) : TestWatcher() {

        override fun starting(description: Description) {
            super.starting(description)
            Dispatchers.setMain(testDispatcher)
        }

        override fun finished(description: Description) {
            super.finished(description)
            Dispatchers.resetMain()
        }
    }
}
