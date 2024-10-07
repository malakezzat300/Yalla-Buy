package com.malakezzat.yallabuy.model

data class DraftOrderRequest(
    val draft_order: DraftOrder,
)

data class DraftOrder(
    val id: Long? = null,
    val note: String, // "shoppingCart"
    val line_items: List<LineItem>,
    //val customer: Customer,
    //val shipping_address: Address? = null,
    //val save: Save,
    //val use_customer_default_address: Boolean,
    val email : String,
)

data class LineItem(
    val title: String,
    val price : String,
    val variant_id: Long,
    val quantity: Int,
    //val properties :Properties
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

data class Save(
    val update : Boolean = true
)

data class Properties(
    val properties: List<Property>,
)

data class Property(
    val name: String,
    val value: String,
)

data class DraftOrderResponse(
    val draft_order: DraftOrder
)

data class DraftOrdersResponse(
    val draft_orders: List<DraftOrder>
)