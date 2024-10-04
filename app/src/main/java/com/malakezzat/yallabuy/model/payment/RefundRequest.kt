package com.malakezzat.paymenttest2.model

data class RefundRequest(
    val payment_id: String,
    val amount_cents: Int
)

data class RefundResponse(
    val id: Int,
    val amount_cents: Int,
    val success: Boolean,
    val currency: String
)