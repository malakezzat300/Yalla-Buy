package com.malakezzat.paymenttest2.model

data class CardPaymentRequest(
    val source: PaymentSource,
    val payment_token: String
)

data class PaymentSource(
    val identifier: String,
    val subtype: String
)

data class PaymentResponse(
    val id: Int,
    val amount_cents: Int,
    val is_refunded: Boolean,
    val pending: Boolean,
    val success: Boolean,
    val currency: String,
    val source_data: SourceData
)

data class SourceData(
    val pan: String,
    val type: String
)
