// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.malakezzat.yallabuy.model

data class Orders (
    val orders: List<Order>
)

data class Order (
    val confirmation_number: String,
    val current_total_discounts_set: Set,
    val line_items: List<LineItemOrder>,
    val presentment_currency: Currency,
    val total_discounts_set: Set,
    val location_id: Long,
    val source_identifier: String,
    val reference: String,
    val number: Long,
    val checkout_id: Long,
    val checkout_token: String,
    val tax_lines: List<TaxLine>,
    val current_total_discounts: String,
    val customer_locale: String,
    val id: Long,
    val app_id: Long,
    val subtotal_price: String,
    val order_status_url: String,
    val current_total_price_set: Set,
    val test: Boolean,
    val total_shipping_price_set: Set,
    val subtotal_price_set: Set,
    val tax_exempt: Boolean,
    val payment_gateway_names: List<String>,
    val total_tax: String,
    val tags: String,
    val current_subtotal_price_set: Set,
    val current_total_tax: String,
    val shipping_lines: List<Any?>,
    val note_attributes: List<Any?>,
    val name: String,
    val total_tax_set: Set,
    val discount_codes: List<Any?>,
    val estimated_taxes: Boolean,
    val note: String,
    val current_subtotal_price: String,
    val current_total_tax_set: Set,
    val total_outstanding: String,
    val order_number: Long,
    val discount_applications: List<Any?>,
    val created_at: String,
    val total_line_items_price_set: Set,
    val taxes_included: Boolean,
    val buyer_accepts_marketing: Boolean,
    val confirmed: Boolean,
    val total_weight: Long,
    val contact_email: String,
    val refunds: List<Any?>,
    val total_discounts: String,
    val fulfillments: List<Any?>,
    val client_details: ClientDetails,
    val updated_at: String,
    val processed_at: String,
    val currency: Currency,
    val browser_ip: String,
    val email: String,
    val source_name: String,
    val total_price_set: Set,
    val total_price: String,
    val total_line_items_price: String,
    val total_tip_received: String,
    val token: String,
    val current_total_price: String,
    val admin_graphql_api_id: String,
    val financial_status: String,
    val customer: CustomerOrder
)

data class ClientDetails (
    val browser_ip: String
)

enum class Currency {
    EGP
}

data class Set (
    val shop_money: Money,
    val presentment_money: Money
)

data class Money (
    val amount: String,
    val currency_code: Currency
)

data class CustomerOrder (
    val tax_exempt: Boolean,
    val email_marketing_consent: EmailMarketingConsentOrder,
    val created_at: String,
    val verified_email: Boolean,
    val tags: String,
    val updated_at: String,
    val accepts_marketing: Boolean,
    val admin_graphql_api_id: String,
    val tax_exemptions: List<Any?>,
    val currency: Currency,
    val id: Long,
    val state: String,
    val marketing_opt_in_level: String,
    val email: String
)

data class EmailMarketingConsentOrder (
    val state: String,
    val opt_in_level: String
)

data class LineItemOrder (
    val variant_title: String,
    val total_discount: String,
    val gift_card: Boolean,
    val requires_shipping: Boolean,
    val total_discount_set: Set,
    val title: String,
    val attributed_staffs: List<Any?>,
    val product_exists: Boolean,
    val variant_id: Long,
    val tax_lines: List<TaxLine>,
    val price: String,
    val vendor: String,
    val product_id: Long,
    val id: Long,
    val grams: Long,
    val sku: String,
    val fulfillable_quantity: Long,
    val quantity: Long,
    val fulfillment_service: String,
    val taxable: Boolean,
    val variant_inventory_management: String,
    val discount_allocations: List<Any?>,
    val admin_graphql_api_id: String,
    val name: String,
    val price_set: Set,
    val properties: List<Any?>,
    val duties: List<Any?>
)

data class TaxLine (
    val channel_liable: Boolean,
    val rate: Double,
    val price: String,
    val price_set: Set,
    val title: String
)
