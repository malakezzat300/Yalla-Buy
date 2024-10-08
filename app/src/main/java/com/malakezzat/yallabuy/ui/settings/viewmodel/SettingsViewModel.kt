package com.malakezzat.yallabuy.ui.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Address
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomerAddress
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: ProductsRepository): ViewModel() {

    private val _conversionRate = MutableStateFlow<ApiState<CurrencyResponse?>>(ApiState.Loading)
    val conversionRate = _conversionRate.asStateFlow()


    private val _userAddresses = MutableStateFlow<ApiState<List<Address>>>(ApiState.Loading)
    val userAddresses = _userAddresses.asStateFlow()

    private val _addressDetails = MutableStateFlow<ApiState<CustomerAddress?>>(ApiState.Loading)
    val addressDetails= _addressDetails.asStateFlow()

    private val _customerAddress = MutableStateFlow<ApiState<CustomerAddress?>>(ApiState.Loading)
    val customerAddress= _customerAddress.asStateFlow()

    private val _deleteAddressEvent = MutableSharedFlow<Unit>()
    val deleteAddressEvent: SharedFlow<Unit> = _deleteAddressEvent.asSharedFlow()


    private val _userId = MutableStateFlow<Long?>(0)
    val userId: StateFlow<Long?> = _userId.asStateFlow()

    private val _userEmail = MutableStateFlow<String?>("")
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()


    fun getRate(){
        Log.i("currencyTest", "getRate: getting rate")
        viewModelScope.launch {
            repository.getConversionRate().onStart {
                _conversionRate.value = ApiState.Loading
            }
                .catch { e ->
                    _conversionRate.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { response ->
                    _conversionRate.value = ApiState.Success(response)
                }
        }
    }



    fun addNewAddress(customerId: Long,address: AddressRequest) {
        viewModelScope.launch {
            try {
                _customerAddress.emit(ApiState.Loading)
                repository.addNewAddress(customerId,address)
                    .collect { newAddress ->
                        _customerAddress.emit(ApiState.Success(newAddress))
                    }
            } catch (e: Exception) {
                _customerAddress.emit(ApiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun getUserAddresses(customerId: Long) {
        viewModelScope.launch {
            try {
                _userAddresses.emit(ApiState.Loading)
                repository.getUserAddresses(customerId)
                    .collect { addresses ->
                        _userAddresses.emit(ApiState.Success(addresses))
                    }
            } catch (e: Exception) {
                _userAddresses.emit(ApiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }


    fun getAddressDetails(customerId: Long, addressId: Long) {
        viewModelScope.launch {
            try {
                _addressDetails.emit(ApiState.Loading)
                repository.getAddressDetails(customerId, addressId)
                    .collect { addressDetail ->
                        _addressDetails.emit(ApiState.Success(addressDetail))
                    }
            } catch (e: Exception) {
                _addressDetails.emit(ApiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun updateUserAddress(customerId: Long, addressId: Long,address: AddressRequest) {
        viewModelScope.launch {
            try {
                _customerAddress.emit(ApiState.Loading)
                repository.updateUserAddress(customerId, addressId, address)
                    .collect { updatedAddress ->
                        _customerAddress.emit(ApiState.Success(updatedAddress))
                    }
            } catch (e: Exception) {
                _customerAddress.emit(ApiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun setDefaultAddress(customerId: Long, addressId: Long) {
        viewModelScope.launch {
            try {
                _customerAddress.emit(ApiState.Loading)
                repository.setDefaultAddress(customerId, addressId)
                    .collect { defaultAddress ->
                        _customerAddress.emit(ApiState.Success(defaultAddress))
                    }
            } catch (e: Exception) {
                _customerAddress.emit(ApiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    fun deleteAddress(customerId: Long, addressId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteAddress(customerId, addressId)
            } catch (e: Exception) {
                Log.i("addressTest", "deleteAddress: $e")
            }
        }
    }



    fun getUserId() {
        viewModelScope.launch {
            val id = repository.getUserId()
            _userId.emit(id)
        }
    }

    fun setUserId(id: Long) {
        viewModelScope.launch {
            repository.setUserId(id)
            _userId.emit(id)
        }
    }

    fun getUserEmail() {
        viewModelScope.launch {
            val email = repository.getUserEmail()
            _userEmail.emit(email)
        }
    }

    fun setUserEmail(email: String) {
        viewModelScope.launch {
            repository.setUserEmail(email)
            _userEmail.emit(email)
        }
    }
}