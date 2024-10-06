package com.malakezzat.yallabuy.data.remote.coupons

data class PriceRule(
    val id: Long? = null,
    val title: String,
    val target_type: String,
    val value_type: String,
    val value: Double,
    val starts_at: String,
    val ends_at: String,
    val target_selection: String,
    val allocation_method: String,
    val customer_selection: String,
    val usage_limit: Long
)

data class priceRuleResponse(
    val price_rules: List<PriceRule>
)

data class priceRuleRequest(
    val price_rule: PriceRule
)

data class DiscountCode(
    val id: Long? = null,
    val code: String,
    val usage_count: Long,
    val created_at: String
)

 data class DiscountCodeResponse(
     val discount_codes: List<DiscountCode>
 )

data class DiscountCodeRequest(
    val discount_code: DiscountCode
)