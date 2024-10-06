package com.malakezzat.paymenttest2

import android.util.Log
import com.malakezzat.paymenttest2.model.AuthRequest
import com.malakezzat.paymenttest2.model.CardPaymentRequest
import com.malakezzat.paymenttest2.model.OrderRequest
import com.malakezzat.paymenttest2.model.OrderResponse
import com.malakezzat.paymenttest2.model.PaymentKeyRequest
import com.malakezzat.paymenttest2.model.PaymentResponse
import com.malakezzat.paymenttest2.model.PaymentStatusResponse
import com.malakezzat.paymenttest2.model.RefundRequest
import com.malakezzat.paymenttest2.model.RefundResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PaymentRemoteDataSourceImpl : PaymentRemoteDataSource {

    private val paymentService: PaymobApiService by lazy {
        RetrofitHelper.getInstance().create(PaymobApiService::class.java)
    }

    override suspend fun getAuthToken(): Flow<String?> = flow {
        try {
            val response = paymentService.getAuthToken()
            emit(response.token)
        } catch (e: Exception) {
            Log.i("paymentTestLog", "getAuthToken: $e")
        }
    }

    override suspend fun createOrder(authToken: String, orderRequest: OrderRequest): Flow<OrderResponse?> = flow {
        try {
            val response = paymentService.createOrder("Bearer $authToken", orderRequest)
            emit(response)
        } catch (e: Exception) {
            Log.i("paymentTestLog", "createOrder: $e")
            emit(null)
        }
    }

    override suspend fun getPaymentKey(paymentKeyRequest: PaymentKeyRequest): String? {
        return try {
            val response = paymentService.getPaymentKey(paymentKeyRequest)
            response.token
        } catch (e: Exception) {
            Log.i("paymentTestLog", "getPaymentKey: $e")
            null
        }
    }

    override suspend fun processCardPayment(cardPaymentRequest: CardPaymentRequest): PaymentResponse? {
        return try {
            paymentService.processCardPayment(cardPaymentRequest)
        } catch (e: Exception) {
            Log.i("paymentTestLog", "processCardPayment: $e")
            null
        }

    }

    override suspend fun getPaymentStatus(paymentId: String): PaymentStatusResponse? {
        return try {
            paymentService.getPaymentStatus(paymentId)
        } catch (e: Exception) {
            Log.i("paymentTestLog", "getPaymentStatus: $e")
            null
        }
    }

    override suspend fun refundPayment(refundRequest: RefundRequest): RefundResponse? {
        return try {
            paymentService.refundPayment(refundRequest)
        } catch (e: Exception) {
            Log.i("paymentTestLog", "refundPayment: $e")
            null
        }
    }
}
