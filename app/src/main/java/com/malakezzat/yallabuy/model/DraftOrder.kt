package com.malakezzat.yallabuy.model

data class DraftOrderRequest(
    val draft_order: DraftOrder,
)

data class DraftOrder(
    val id: Long? = null,
    val note: String = "",
    val line_items: List<LineItem> = emptyList(),
    val email: String = "",
    val customer: Customer? = null,
    var total_tax : String  = "0.0",
    var total_price : String  = "0.0",
    var subtotal_price : String = "0.0",
    var applied_discount : AppliedDiscount? = AppliedDiscount(),
)

data class LineItem(
    val title: String,
    val price : String,
    val variant_id: Long,
    var quantity: Int,
    val properties : List<Property>,
    val product_id : Long,

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

data class AppliedDiscount(
    val title : String = "title",
    val description : String = "Description ",
    val value : String = "0" ,
    val value_type : String = "percentage",
    val amount : String = "0.0",

)