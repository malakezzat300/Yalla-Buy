package com.malakezzat.yallabuy.model

data class DraftOrderRequest(
    val draft_order: DraftOrder,
)

data class DraftOrder(
    val id: Long? = null,
    val note: String = "",
    val line_items: List<LineItem> = emptyList(),
    val email: String = ""
)

data class LineItem(
    val title: String,
    val price : String,
    val variant_id: Long,
    var quantity: Int,
    val properties : List<Property>,
    val product_id : Long
)

data class Customer(
    val id: Long
)

data class Save(
    val update : Boolean = true
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