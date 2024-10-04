package com.malakezzat.paymenttest2

import com.malakezzat.paymenttest2.model.AuthRequest
import com.malakezzat.paymenttest2.model.AuthResponse
import com.malakezzat.paymenttest2.model.CardPaymentRequest
import com.malakezzat.paymenttest2.model.OrderRequest
import com.malakezzat.paymenttest2.model.OrderResponse
import com.malakezzat.paymenttest2.model.PaymentKeyRequest
import com.malakezzat.paymenttest2.model.PaymentKeyResponse
import com.malakezzat.paymenttest2.model.PaymentResponse
import com.malakezzat.paymenttest2.model.PaymentStatusResponse
import com.malakezzat.paymenttest2.model.RefundRequest
import com.malakezzat.paymenttest2.model.RefundResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface PaymobApiService {

    private val token : String
        get() = "ZXlKaGJHY2lPaUpJVXpVeE1pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmpiR0Z6Y3lJNklrMWxjbU5vWVc1MElpd2ljSEp2Wm1sc1pWOXdheUk2T1RrNU1ETXlMQ0p1WVcxbElqb2lhVzVwZEdsaGJDSjkuS3ppcEdTb0hUdGM5UE1pNC1OUDJWc0dFdFVaUlFXTUxsWFlGZjJsVFJHcFZYTTQwTzh3MFZJdWVpYndiWm44Z3JZVVBXSU40aEhDa3ltVW1OSi0zelE="

    @POST("auth/tokens")
    suspend fun getAuthToken(@Body body: AuthRequest): AuthResponse

    @POST("ecommerce/orders")
    suspend fun createOrder(@Header("Authorization") token: String, @Body body: OrderRequest): OrderResponse

    @POST("acceptance/payment_keys")
    suspend fun getPaymentKey(@Body body: PaymentKeyRequest): PaymentKeyResponse

    @POST("acceptance/payments/pay")
    suspend fun processCardPayment(@Body body: CardPaymentRequest): PaymentResponse

    @GET("acceptance/payments/{payment_id}")
    suspend fun getPaymentStatus(@Path("payment_id") paymentId: String): PaymentStatusResponse

    @POST("acceptance/void_refund/refund")
    suspend fun refundPayment(@Body body: RefundRequest): RefundResponse

}
