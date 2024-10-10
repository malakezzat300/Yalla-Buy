package com.malakezzat.yallabuy.model

data class PriceRule(
    val id: Long? = null,
    val title: String = "",
    val target_type: String = "",
    val value_type: String= "",
    val value: Double = 0.0,
    val starts_at: String= "",
    val ends_at: String = "",
    val target_selection: String= "",
    val allocation_method: String= "",
    val customer_selection: String= "",
    val usage_limit: Long = 0L
)

data class PriceRulesResponse(
    val price_rules: List<PriceRule>
)

data class PriceRuleResponse(
    val price_rule: PriceRule
)

data class PriceRuleRequest(
    val price_rule: PriceRule
)

data class DiscountCode(
    val id: Long? = null,
    val code: String = "",
    val usage_count: Long = 0L,
    val created_at: String = "",
    val price_rule_id : Long = 0L,
)

 data class DiscountCodesResponse(
     val discount_codes: List<DiscountCode>
 )

data class DiscountCodeRequest(
    val discount_code: DiscountCode
)

data class DiscountCodeResponse(
    val discount_code: DiscountCode
)