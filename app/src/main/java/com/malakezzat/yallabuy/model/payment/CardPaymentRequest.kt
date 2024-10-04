package com.malakezzat.paymenttest2.model

data class CardPaymentRequest(
    val source: PaymentSource,      // Card payment source details
    val payment_token: String       // The payment key token received from getPaymentKey
)

data class PaymentSource(
    val identifier: String,         // Card number or token
    val subtype: String             // "TOKEN" for card tokenization
)

data class PaymentResponse(
    val id: Int,                    // Payment ID
    val amount_cents: Int,          // Amount in cents
    val is_refunded: Boolean,       // Whether the payment was refunded
    val pending: Boolean,           // Whether the payment is still pending
    val success: Boolean,           // Whether the payment was successful
    val currency: String,           // Currency code
    val source_data: SourceData     // Payment source details
)

data class SourceData(
    val pan: String,                // Masked card number (e.g., **** **** **** 1234)
    val type: String                // Type of card (e.g., credit, debit)
)
