package com.malakezzat.yallabuy.ui.orders.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class OrdersViewModel (private val repository: ProductsRepository): ViewModel() {

    private val _customerDataByEmail = MutableStateFlow<ApiState<CustomerSearchRespnse>>(ApiState.Loading)
    val customerDataByEmail = _customerDataByEmail.asStateFlow()


    init {
        getUserByEmail()
    }
    fun getUserByEmail(){
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        viewModelScope.launch {
            if (userEmail != null) {
                repository.getCustomerByEmail(userEmail)
                    .onStart {
                        _customerDataByEmail.value = ApiState.Loading // Set loading state
                    }
                    .catch { e ->
                        _customerDataByEmail.value = ApiState.Error(e.message ?: "Unknown error")
                        // _errorMessage.value = e.message // Set error message
                        Log.i("TAG", "getCustomer: error ${e.message}")

                    }
                    .collect { customerData ->
                        _customerDataByEmail.value = ApiState.Success(customerData) // Set success state with data
                        Log.i("TAG", "getCustomer: ${customerData.customers.get(0).id}")
                    }
            }
        }
    }
}