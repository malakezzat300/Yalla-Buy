package com.malakezzat.yallabuy.model

data class DraftOrder(
    val id: Long? = null,
    val line_items: List<LineItem>,
    val customer: Customer? = null,
    val shipping_address: Address? = null
)

data class LineItem(
    val title: String,
    val price : String,
    val variant_id: Long,
    val quantity: Int
)

data class Customer(
    val id: Long
)

data class Address(
    val first_name: String,
    val last_name: String,
    val address1: String,
    val city: String,
    val province: String,
    val country: String,
    val zip: String
)

data class DraftOrderResponse(
    val draft_order: DraftOrder
)

data class DraftOrdersResponse(
    val draft_orders: List<DraftOrder>
)