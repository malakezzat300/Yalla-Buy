package com.malakezzat.paymenttest2.model

data class PaymentKeyRequest(
    val amount_cents: Int,
    val currency: String,
    val order_id: String,
    val billing_data: BillingData,
    val integration_id: Int,
    val lock_order_when_paid: String
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
    val token: String
)
