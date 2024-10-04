package com.malakezzat.paymenttest2.model

data class RefundRequest(
    val payment_id: String,         // The ID of the payment to refund
    val amount_cents: Int           // Amount to refund in cents
)

data class RefundResponse(
    val id: Int,                    // Refund ID
    val amount_cents: Int,          // Refunded amount in cents
    val success: Boolean,           // Whether the refund was successful
    val currency: String            // Currency code
)