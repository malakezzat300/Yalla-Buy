package com.malakezzat.yallabuy.model

import com.google.gson.annotations.SerializedName

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
    val created_at: String,
    val orders_count:Int=0
)

data class Address (

    var id           : Long?     = null,
    var customer_id   : Long?     = null,
    var first_name    : String?  = null,
    var last_name     : String?  = null,
    var company      : String?  = null,
    var address1     : String?  = null,
    var address2     : String?  = null,
    var city         : String?  = null,
    var province     : String?  = null,
    var country      : String?  = null,
    var zip          : String?  = null,
    var phone        : String?  = null,
    var name         : String?  = null,
    var province_code : String?  = null,
    var country_code  : String?  = null,
    var country_name  : String?  = country,
    var default      : Boolean? = null

)

data class CustomerId (
    val id: Long
)

data class AddressRequest(
    val address : Address
)

data class EmailMarketingConsent (

    var state            : String? = null,
    var opt_in_level       : String? = null,
    var consent_updated_at : String? = null
)
data class SmsMarketingConsent (

  var state                : String? = null,
  var opt_in_level           : String? = null,
  var consent_updated_at     : String? = null,
  var consent_collected_from : String? = null

)

data class Customers (

    @SerializedName("id"                           ) var id                        : Long?                   = null,
    @SerializedName("email"                        ) var email                     : String?                = null,
    @SerializedName("created_at"                   ) var createdAt                 : String?                = null,
    @SerializedName("updated_at"                   ) var updatedAt                 : String?                = null,
    @SerializedName("first_name"                   ) var firstName                 : String?                = null,
    @SerializedName("last_name"                    ) var lastName                  : String?                = null,
    @SerializedName("orders_count"                 ) var ordersCount               : Int?                   = null,
    @SerializedName("state"                        ) var state                     : String?                = null,
    @SerializedName("total_spent"                  ) var totalSpent                : String?                = null,
    @SerializedName("last_order_id"                ) var lastOrderId               : Long?                   = null,
    @SerializedName("note"                         ) var note                      : String?                = null,
    @SerializedName("verified_email"               ) var verifiedEmail             : Boolean?               = null,
    @SerializedName("multipass_identifier"         ) var multipassIdentifier       : String?                = null,
    @SerializedName("tax_exempt"                   ) var taxExempt                 : Boolean?               = null,
    @SerializedName("tags"                         ) var tags                      : String?                = null,
    @SerializedName("last_order_name"              ) var lastOrderName             : String?                = null,
    @SerializedName("currency"                     ) var currency                  : String?                = null,
    @SerializedName("phone"                        ) var phone                     : String?                = null,
    @SerializedName("addresses"                    ) var addresses                 : ArrayList<Address>     = arrayListOf(),
    @SerializedName("accepts_marketing"            ) var acceptsMarketing          : Boolean?               = null,
    @SerializedName("accepts_marketing_updated_at" ) var acceptsMarketingUpdatedAt : String?                = null,
    @SerializedName("marketing_opt_in_level"       ) var marketingOptInLevel       : String?                = null,
    @SerializedName("tax_exemptions"               ) var taxExemptions             : ArrayList<String>      = arrayListOf(),
    @SerializedName("email_marketing_consent"      ) var emailMarketingConsent     : EmailMarketingConsent? = EmailMarketingConsent(),
    @SerializedName("sms_marketing_consent"        ) var smsMarketingConsent       : SmsMarketingConsent?   = SmsMarketingConsent(),
    @SerializedName("admin_graphql_api_id"         ) var adminGraphqlApiId         : String?                = null,
    @SerializedName("default_address"              ) var defaultAddress            : Address?               = Address()

)

data class CustomerSearchRespnse(
    val customers: List<Customers>
)


data class CustomerAddress(
    val customer_address: Address,
)