package com.malakezzat.yallabuy.ui.auth.viewmodel

import com.malakezzat.yallabuy.data.FakeRepository
import com.malakezzat.yallabuy.data.remot.FakeRemoteDataSource
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Customer
import com.malakezzat.yallabuy.model.CustomerDetails
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.Customerr
import com.malakezzat.yallabuy.ui.shoppingcart.viewmodel.ShoppingCartViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`


class SignUpViewModelTest{

    lateinit var signUpViewModel: SignUpViewModel
    private lateinit var fakeRemoteDataSource : FakeRemoteDataSource
    private lateinit var repository : FakeRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRemoteDataSource = FakeRemoteDataSource()
        repository = FakeRepository()
        signUpViewModel = SignUpViewModel(repository)
    }

    @Test
    fun createCustomer_customerRequest_CustomerData() = runTest {
        // Arrange

        val customerRequest = CustomerRequest(Customerr("testName", "","tastName@gmail.com"))

        // Act
        signUpViewModel.createCustomer(customerRequest)
        advanceUntilIdle()
        // Assert
        val result = signUpViewModel.customerData.first()

        assertTrue(result is ApiState.Success)
        assertEquals((result as ApiState.Success).data.customer.first_name, "testName")
        assertEquals(result.data.customer.email, "tastName@gmail.com")
    }
}