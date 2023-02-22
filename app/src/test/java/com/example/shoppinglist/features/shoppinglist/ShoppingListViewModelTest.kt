package com.example.shoppinglist.features.shoppinglist

import app.cash.turbine.test
import com.example.shoppinglist.data.PreferencesManager
import com.example.shoppinglist.data.local.models.ShoppingEntity
import com.example.shoppinglist.data.repos.ShoppingListRepo
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
internal class ShoppingListViewModelTest {

    private lateinit var shoppingListViewModel: ShoppingListViewModel

    @MockK
    private lateinit var repo: ShoppingListRepo

    @RelaxedMockK
    private lateinit var preferencesManager: PreferencesManager

    private val dispatcher = UnconfinedTestDispatcher()


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        shoppingListViewModel = ShoppingListViewModel(repo, preferencesManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    //region navigationTest

    @Test
    fun `when onAddItemClicked is called, AddItem is emitted`() = runTest(dispatcher) {
        val expected = ShoppingListNavigation.AddItem

        shoppingListViewModel.navigationSharedFlow.test {
            shoppingListViewModel.onAddClicked()
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `when onDetailsClicked is called, ItemDetails is emitted`() = runTest(dispatcher) {
        val shoppingEntity = ShoppingEntity("test item", "1", "test entity item", false)
        val expected = ShoppingListNavigation.ItemDetails(shoppingEntity)

        shoppingListViewModel.navigationSharedFlow.test {
            shoppingListViewModel.onDetailsClicked(shoppingEntity)
            assertEquals(expected, awaitItem())
        }
    }

    //endregion

}