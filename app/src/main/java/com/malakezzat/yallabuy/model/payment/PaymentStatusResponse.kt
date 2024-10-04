package com.malakezzat.paymenttest2.model

data class PaymentStatusResponse(
    val id: Int,
    val amount_cents: Int,
    val success: Boolean,
    val is_refunded: Boolean,
    val pending: Boolean,
    val source_data: SourceData,
    val currency: String
)
