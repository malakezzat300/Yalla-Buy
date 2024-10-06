package com.malakezzat.paymenttest2.model

data class PaymentKeyRequest(
    val amount_cents: Int,
    val currency: String,
    val order_id: String,
    val billing_data: BillingData
)

data class BillingData(
    val apartment: String,
    val email: String,
    val floor: String,
    val first_name: String,
    val last_name: String,
    val phone_number: String,
    val shipping_method: String,
    val street: String,
    val building: String,
    val postal_code: String,
    val city: String,
    val country: String
)

data class PaymentKeyResponse(
    val token: String,
    val id: String,
    val iframe_id: String,
    val url: String
)
