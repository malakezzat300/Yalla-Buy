package com.malakezzat.yallabuy.ui.payment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.paymenttest2.PaymentRemoteDataSource
import com.malakezzat.paymenttest2.model.CardPaymentRequest
import com.malakezzat.paymenttest2.model.OrderRequest
import com.malakezzat.paymenttest2.model.OrderResponse
import com.malakezzat.paymenttest2.model.PaymentKeyRequest
import com.malakezzat.paymenttest2.model.PaymentKeyResponse
import com.malakezzat.paymenttest2.model.PaymentResponse
import com.malakezzat.paymenttest2.model.PaymentStatusResponse
import com.malakezzat.paymenttest2.model.RefundRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PaymentViewModel(private val paymentRemoteDataSource: PaymentRemoteDataSource) : ViewModel() {

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> get() = _authToken

    private val _orderResponse = MutableStateFlow<OrderResponse?>(null)
    val orderResponse: StateFlow<OrderResponse?> get() = _orderResponse

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    private val _paymentKey = MutableStateFlow<String?>(null)
    val paymentKey: StateFlow<String?> = _paymentKey

    private val _paymentStatus = MutableStateFlow<PaymentStatusResponse?>(null)
    val paymentStatus: StateFlow<PaymentStatusResponse?> = _paymentStatus

    private val _payment = MutableStateFlow<PaymentResponse?>(null)
    val payment: StateFlow<PaymentResponse?> = _payment

    fun fetchToken() {
        viewModelScope.launch {
            getAuthToken()
        }
    }

    fun getAuthToken() {
        viewModelScope.launch {
            paymentRemoteDataSource.getAuthToken().collect { token ->
                if (token != null) {
                    _authToken.value = token
                } else {
                    _errorMessage.value = "Failed to get auth token."
                }
            }
        }
    }

    fun createOrder(authToken: String, orderRequest: OrderRequest) {
        viewModelScope.launch {
            paymentRemoteDataSource.createOrder(authToken, orderRequest).collect { response ->
                if (response != null) {
                    _orderResponse.value = response
                } else {
                    _errorMessage.value = "Failed to create order."
                }
            }
        }
    }

    fun fetchPaymentKey(paymentKeyRequest: PaymentKeyRequest) {
        viewModelScope.launch {
            val result = paymentRemoteDataSource.getPaymentKey(paymentKeyRequest)
            _paymentKey.value = result
            if(result != null){
                _paymentKey.value = result
            } else {
                _errorMessage.value = "Failed to Fetch Payment Key."
            }
        }
    }

    fun processPayment(cardPaymentRequest: CardPaymentRequest) {
        viewModelScope.launch {
            val result = paymentRemoteDataSource.processCardPayment(cardPaymentRequest)
            if(result != null){
                _payment.value = result
            } else {
                _errorMessage.value = "Failed to Process Payment."
            }
        }
    }

    fun fetchPaymentStatus(paymentId: String) {
        viewModelScope.launch {
            val result = paymentRemoteDataSource.getPaymentStatus(paymentId)
            if(result != null){
                _paymentStatus.value = result
            } else {
                _errorMessage.value = "Failed to Fetch Payment Status."
            }
        }
    }

    fun refundPayment(refundRequest: RefundRequest) {
        viewModelScope.launch {
            paymentRemoteDataSource.refundPayment(refundRequest)

        }
    }
}