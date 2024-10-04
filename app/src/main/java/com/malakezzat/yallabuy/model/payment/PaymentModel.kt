package com.malakezzat.paymenttest2.model

data class AuthRequest(
    val api_key: String
)

data class AuthResponse(
    val token: String
)

data class OrderRequest(
    var auth_token: String,
    val delivery_details: DeliveryDetails,
    val amount_cents: Long,
    val currency: String,
    val items: List<Item>
)

data class OrderResponse(
    val id: String,
    val amount_cents: Long,
    // Add more fields as needed
)

data class DeliveryDetails(
    val apartment: String,
    val email: String,
    val floor: String,
    val phone_number: String,
    val shipping_method: String,
    val city: String,
    val country: String,
    val first_name: String,
    val last_name: String,
    val state: String,
    val street: String,
    val postal_code: String
)

data class Item(
    val name: String,
    val amount_cents: Long,
    val quantity: Int
)