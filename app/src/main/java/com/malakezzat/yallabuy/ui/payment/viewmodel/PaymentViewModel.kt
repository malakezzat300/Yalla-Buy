package com.malakezzat.yallabuy.ui.payment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.AddressResponse
import com.malakezzat.yallabuy.model.CustomerAddress
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.VariantResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class PaymentViewModel(private val repository: ProductsRepository) : ViewModel() {

    private val TAG = "PaymentViewModel"

    private val _draftOrders = MutableStateFlow<ApiState<DraftOrdersResponse>>(ApiState.Loading)
    val draftOrders : StateFlow<ApiState<DraftOrdersResponse>> get() = _draftOrders

    private val _singleDraftOrders = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val singleDraftOrders : StateFlow<ApiState<DraftOrderResponse>> get() = _singleDraftOrders

    private val _shoppingCartDraftOrder = MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val shoppingCartDraftOrder = _shoppingCartDraftOrder.asStateFlow()

    private val _variantId = MutableStateFlow<ApiState<VariantResponse>>(ApiState.Loading)
    val variantId = _variantId.asStateFlow()

    private val _userAddresses = MutableStateFlow<ApiState<AddressResponse>>(ApiState.Loading)
    val userAddresses = _userAddresses.asStateFlow()

    private val _addressDetails = MutableStateFlow<ApiState<CustomerAddress?>>(ApiState.Loading)
    val addressDetails= _addressDetails.asStateFlow()

    private val _customerAddress = MutableStateFlow<ApiState<CustomerAddress?>>(ApiState.Loading)
    val customerAddress= _customerAddress.asStateFlow()

    private val _deleteAddressEvent = MutableSharedFlow<String>()
    val deleteAddressEvent: SharedFlow<String> = _deleteAddressEvent.asSharedFlow()

    private val _defaultAddressEvent = MutableStateFlow<ApiState<CustomerAddress>>(ApiState.Loading)
    val defaultAddressEvent= _defaultAddressEvent.asStateFlow()



    fun getDraftOrders(){
        viewModelScope.launch {
            repository.getAllDraftOrders().onStart {
                _draftOrders.value = ApiState.Loading
            }
                .catch { e ->
                    _draftOrders.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { draftOrdersResponse ->
                    val draftOrders = draftOrdersResponse.draft_orders.filter {
                        it.email == FirebaseAuth.getInstance().currentUser?.email
                    }
                    val shoppingCartDraftOrder = draftOrders.filter{
                        it.note == "shoppingCart"
                    }
                    if (shoppingCartDraftOrder.isNotEmpty()) {
                        _shoppingCartDraftOrder.value = ApiState.Success(draftOrders.filter{
                            it.note == "shoppingCart"
                        }[0])
                    } else {
                        _shoppingCartDraftOrder.value = ApiState.Error("shoppingCart not found")
                    }
                }
        }
    }

    fun getDraftOrder(draftOrderId: Long) {
        viewModelScope.launch {
            repository.getDraftOrder(draftOrderId)
                .onStart {
                    _singleDraftOrders.value = ApiState.Loading
                }
                .catch { e ->
                    _singleDraftOrders.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { draftOrderResponse ->
                    _singleDraftOrders.value = ApiState.Success(draftOrderResponse)
                }
        }
    }

    fun finalizeDraftOrder(draftOrderId: Long) {
        viewModelScope.launch {
            repository.finalizeDraftOrder(draftOrderId)
                .onStart {
                    _singleDraftOrders.value = ApiState.Loading
                }
                .catch { e ->
                    _singleDraftOrders.value = ApiState.Error(e.message ?: "Failed to finalize draft order")
                }
                .collect { response ->
                    _singleDraftOrders.value = ApiState.Success(response)
                    getDraftOrders()
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

    fun setDefaultAddress(customerId: Long, addressId: Long) {
        viewModelScope.launch {
            try {
                _defaultAddressEvent.emit(ApiState.Loading)
                repository.setDefaultAddress(customerId, addressId)
                    .collect { updatedAddress ->
                        _defaultAddressEvent.emit(ApiState.Success(updatedAddress))
                        Log.i("paymentTest", "setDefaultAddress: ${updatedAddress.customer_address.default}")
                    }
            } catch (e: Exception) {
                _defaultAddressEvent.emit(ApiState.Error(e.message ?: "Unknown Error"))
            }
        }
    }


}