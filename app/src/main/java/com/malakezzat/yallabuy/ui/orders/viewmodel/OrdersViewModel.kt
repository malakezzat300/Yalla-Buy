package com.malakezzat.yallabuy.ui.orders.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.Order
import com.malakezzat.yallabuy.model.Orders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class OrdersViewModel (val repository: ProductsRepository): ViewModel() {
    private val TAG = "OrdersViewModel"
    private val _customerDataByEmail = MutableStateFlow<ApiState<CustomerSearchRespnse>>(ApiState.Loading)
    val customerDataByEmail = _customerDataByEmail.asStateFlow()

    private val _customerOrders = MutableStateFlow<ApiState<List<Order>>>(ApiState.Loading)
    val customerOrders = _customerOrders.asStateFlow()


    init {
        getUserByEmail()
        getAllOrdersForCustomerByID(7713903837366)
    }
    fun getUserByEmail(){
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail != null) {
            repository.setUserEmail(userEmail)
        }
        viewModelScope.launch {
            if (userEmail != null) {
                repository.getCustomerByEmail(userEmail)
                    .onStart {
                        _customerDataByEmail.value = ApiState.Loading // Set loading state
                    }
                    .catch { e ->
                        _customerDataByEmail.value = ApiState.Error(e.message ?: "Unknown error")
                        // _errorMessage.value = e.message // Set error message
                        Log.i(TAG, "getCustomer: error ${e.message}")

                    }
                    .collect { customerData ->
                        _customerDataByEmail.value = ApiState.Success(customerData) // Set success state with data
                        Log.i(TAG, "getCustomer: ${customerData.customers.get(0).id}")
                        customerData.customers.get(0).id?.let { repository.setUserId(it) }
                    }
            }
        }
    }

    //7713903837366 test id
    fun getAllOrdersForCustomerByID(id :Long){
        viewModelScope.launch {
            //add a temporary permanent email
            repository.getAllOrdersForCustomerByID(7713903837366)
                .onStart {
                    _customerOrders.value = ApiState.Loading
                }.catch { e->
                    _customerOrders.value = ApiState.Error(e.message ?: "Unknown error")
                    // _errorMessage.value = e.message // Set error message
                    Log.i(TAG, "getAllOrdersForCustomerByID: error ${e.message}")
                }.collect{customerOrders->
                    _customerOrders.value = ApiState.Success(customerOrders)
                    Log.i(TAG, "getAllOrdersForCustomerByID: ${customerOrders.get(0).line_items}")
                    Log.i(TAG, "getAllOrdersForCustomerByID: ${repository.getUserId()},,,,,,${repository.getUserEmail()}")

                }
        }
    }
}