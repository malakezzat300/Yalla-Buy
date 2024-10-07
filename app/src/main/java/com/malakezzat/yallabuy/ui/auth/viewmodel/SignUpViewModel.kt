package com.malakezzat.yallabuy.ui.auth.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.UserProfileChangeRequest
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SignUpViewModel(var repo : ProductsRepository) : ViewModel(){
    private val _customerData = MutableStateFlow<ApiState<CustomerResponse>>(ApiState.Loading)
    val customerData = _customerData.asStateFlow()

    private val _customerDataByEmail = MutableStateFlow<ApiState<CustomerSearchRespnse>>(ApiState.Loading)
    val customerDataByEmail = _customerDataByEmail.asStateFlow()

    fun createCustomer(customerRequest: CustomerRequest){
        viewModelScope.launch {
            repo.createCustomer(customerRequest)
                .onStart {
                    _customerData.value = ApiState.Loading // Set loading state
                }
                .catch { e ->
                    _customerData.value = ApiState.Error(e.message ?: "Unknown error")
                    // _errorMessage.value = e.message // Set error message
                }
                .collect { customerData ->
                    _customerData.value = ApiState.Success(customerData) // Set success state with data
                    Log.i("TAG", "createCustomer: ${customerData.customer}")
                }
        }
    }

    fun getCustomerByEmail(customerRequest: String){
        viewModelScope.launch {
            repo.getCustomerByEmail(customerRequest)
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
    fun getCustomerById(customerRequest: Long){
        viewModelScope.launch {
            repo.getCustomerById(customerRequest)
                .onStart {
                    _customerDataByEmail.value = ApiState.Loading // Set loading state
                }
                .catch { e ->
                    _customerDataByEmail.value = ApiState.Error(e.message ?: "Unknown error")
                    // _errorMessage.value = e.message // Set error message
                    Log.i("TAG", "getCustomerById: error ${e.message}")

                }
                .collect { customerData ->
                    _customerDataByEmail.value = ApiState.Success(customerData) // Set success state with data
                    Log.i("TAG", "getCustomerById: ${customerData.customers.get(0).email}")
                }
        }
    }
}