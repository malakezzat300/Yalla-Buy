package com.malakezzat.paymenttest2

import com.malakezzat.paymenttest2.model.CardPaymentRequest
import com.malakezzat.paymenttest2.model.OrderRequest
import com.malakezzat.paymenttest2.model.OrderResponse
import com.malakezzat.paymenttest2.model.PaymentKeyRequest
import com.malakezzat.paymenttest2.model.PaymentResponse
import com.malakezzat.paymenttest2.model.PaymentStatusResponse
import com.malakezzat.paymenttest2.model.RefundRequest
import com.malakezzat.paymenttest2.model.RefundResponse
import kotlinx.coroutines.flow.Flow

interface PaymentRemoteDataSource {
    suspend fun getAuthToken(apiKey: String): Flow<String?>
    suspend fun createOrder(authToken: String, orderRequest: OrderRequest): Flow<OrderResponse?>
    suspend fun getPaymentKey(paymentKeyRequest: PaymentKeyRequest): String?
    suspend fun processCardPayment(cardPaymentRequest: CardPaymentRequest): PaymentResponse?
    suspend fun refundPayment(refundRequest: RefundRequest): RefundResponse?
    suspend fun getPaymentStatus(paymentId: String): PaymentStatusResponse?
}