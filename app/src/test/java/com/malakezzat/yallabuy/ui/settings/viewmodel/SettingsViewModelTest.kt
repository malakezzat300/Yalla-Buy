package com.malakezzat.yallabuy.ui.settings.viewmodel

import androidx.lifecycle.asLiveData
import com.malakezzat.yallabuy.data.FakeRepository
import com.malakezzat.yallabuy.data.remot.FakeRemoteDataSource
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.getOrAwaitValue
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsViewModelTest{
    lateinit var settingsViewModel: SettingsViewModel
    private lateinit var repository : FakeRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeRepository()
        settingsViewModel = SettingsViewModel(repository)
    }

    @Test
    fun getRate_returnConversionRate() = runTest {
        settingsViewModel.getRate()
        advanceUntilIdle()

        val apiState = settingsViewModel.conversionRate.first()

        assertEquals(true, apiState is ApiState.Success)
        assertThat(apiState, `is`(ApiState.Success(repository.currencyResponse)))
    }

    @Test
    fun addNewAddress_oldCustomerIdAndAddress_returnNewAddress() = runTest {
        settingsViewModel.addNewAddress(103L, AddressRequest(repository.address333))
        advanceUntilIdle()

        val apiState = settingsViewModel.customerAddress.first()

        assertEquals(true, apiState is ApiState.Success)

        val actualAddresses = (apiState as ApiState.Success).data?.customer_address

        val expectedAddresses = repository.address333

        assertThat(actualAddresses, `is`(expectedAddresses))
    }

    @Test
    fun addNewAddress_newCustomerIdAndAddress_returnNewAddress() = runTest {
        settingsViewModel.addNewAddress(105L, AddressRequest(repository.address333))
        advanceUntilIdle()

        val apiState = settingsViewModel.customerAddress.first()

        assertEquals(true, apiState is ApiState.Success)

        val actualAddresses = (apiState as ApiState.Success).data?.customer_address

        val expectedAddresses = repository.address333

        assertThat(actualAddresses, `is`(expectedAddresses))
    }

    @Test
    fun getUserAddresses_customerId_returnUserAddresses() = runTest {
        settingsViewModel.getUserAddresses(103L)
        advanceUntilIdle()

        val apiState = settingsViewModel.userAddresses.first()

        assertEquals(true, apiState is ApiState.Success)

        val actualAddresses = (apiState as ApiState.Success).data.addresses

        val expectedAddresses = listOf(FakeRepository().address3,FakeRepository().address33)

        assertThat(actualAddresses, `is`(expectedAddresses))
    }

    @Test
    fun getUserAddresses_notExistCustomerId_returnEmptyList() = runTest {
        settingsViewModel.getUserAddresses(0L)
        advanceUntilIdle()

        val apiState = settingsViewModel.userAddresses.first()

        assertEquals(true, apiState is ApiState.Success)

        val actualAddresses = (apiState as ApiState.Success).data.addresses

        assertThat(actualAddresses, `is`(emptyList()))
    }

    @Test
    fun setDefaultAddress_customerIdAndAddressId_returnNewDefaultAddress() = runTest {
        repository.address33.id?.let { settingsViewModel.setDefaultAddress(103L, it) }
        advanceUntilIdle()

        val apiState = settingsViewModel.defaultAddressEvent.first()

        assertEquals(true, apiState is ApiState.Success)

        val actualAddresses = (apiState as ApiState.Success).data.customer_address

        assertThat(actualAddresses, `is`(repository.address33))
    }

    @Test
    fun setDefaultAddress_notExistCustomerIdAndAddressId_returnError() = runTest {
        repository.address33.id?.let { settingsViewModel.setDefaultAddress(108L, it) }
        advanceUntilIdle()

        val apiState = settingsViewModel.defaultAddressEvent.first()

        assertEquals(true, apiState is ApiState.Error)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }
}