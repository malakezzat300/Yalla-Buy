package com.malakezzat.yallabuy.ui.shoppingcart.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.malakezzat.yallabuy.data.FakeRepository
import com.malakezzat.yallabuy.data.remot.FakeRemoteDataSource
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.DraftOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.Is.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShoppingCartViewModelTest{
    lateinit var shoppingCartViewModel: ShoppingCartViewModel
    private lateinit var fakeRemoteDataSource : FakeRemoteDataSource
    private lateinit var repository : FakeRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = FakeRepository()
        shoppingCartViewModel = ShoppingCartViewModel(repository)
    }

    @Test
    fun getDraftOrders_returnDraftOrders() = runTest {
        shoppingCartViewModel.getDraftOrders()

        val apiState = shoppingCartViewModel.draftOrders.first()
        advanceUntilIdle()

        assertThat((apiState as ApiState.Loading), `is`(instanceOf(ApiState::class.java)))
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }
}