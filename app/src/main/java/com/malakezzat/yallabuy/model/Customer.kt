package com.malakezzat.yallabuy.model

data class Customerr (
    val first_name: String?,
    val last_name: String?,
    val email: String,
    val phone: String? = null,
    val verified_email: Boolean = true
    )

data class CustomerRequest(
    val customer: Customerr
)

data class CustomerResponse(
    val customer: CustomerDetails
)

data class CustomerDetails(
    val id: Long,
    val email: String,
    val first_name: String?,
    val last_name: String?,
    val phone: String?,
    val created_at: String
)