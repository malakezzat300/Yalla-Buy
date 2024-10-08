package com.malakezzat.yallabuy.model

data class CurrencyResponse(
    val result: String,
    val documentation: String,
    val terms_of_use: String,
    val time_last_update_unix: Long,
    val time_last_update_utc: String,
    val time_next_update_unix: Long,
    val time_next_update_utc: String,
    val base_code: String,
    val conversion_rates: ConversionRates,
)

data class ConversionRates(
    val EGP: Long,
    val USD: Double,
    val AED: Double,
    val SAR: Double,
    val EUR: Double
)
