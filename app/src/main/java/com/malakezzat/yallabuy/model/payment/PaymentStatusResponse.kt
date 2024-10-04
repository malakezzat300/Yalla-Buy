package com.malakezzat.paymenttest2.model

data class PaymentStatusResponse(
    val id: Int,                    // Payment ID
    val amount_cents: Int,          // Amount in cents
    val success: Boolean,           // Whether the payment was successful
    val is_refunded: Boolean,       // Whether the payment was refunded
    val pending: Boolean,           // Whether the payment is still pending
    val source_data: SourceData,    // Payment source details
    val currency: String            // Currency code
)
