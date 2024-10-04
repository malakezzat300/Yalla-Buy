package com.malakezzat.paymenttest2.model

data class PaymentKeyRequest(
    val amount_cents: Int,          // Amount in cents
    val currency: String,           // Currency code (e.g., EGP, USD)
    val order_id: String,           // Order ID generated after creating an order
    val billing_data: BillingData,  // Billing details for the user
    val integration_id: Int,        // Integration ID for card payments
    val lock_order_when_paid: String // "false" or "true"
)

data class BillingData(
    val apartment: String,
    val email: String,
    val floor: String,
    val first_name: String,
    val street: String,
    val building: String,
    val phone_number: String,
    val shipping_method: String,
    val postal_code: String,
    val city: String,
    val country: String,
    val last_name: String,
    val state: String
)

data class PaymentKeyResponse(
    val token: String  // The payment key token
)
