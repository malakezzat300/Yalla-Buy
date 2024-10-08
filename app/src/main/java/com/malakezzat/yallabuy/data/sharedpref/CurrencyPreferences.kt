package com.malakezzat.yallabuy.data.sharedpref

import android.content.Context
import android.content.SharedPreferences

class CurrencyPreferences private constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "currency_prefs"
        private const val KEY_EXCHANGE_RATE = "exchange_rate"
        private const val KEY_BASE_CURRENCY = "base_currency"
        private const val KEY_TARGET_CURRENCY = "target_currency"

        @Volatile
        private var instance: CurrencyPreferences? = null

        fun getInstance(context: Context): CurrencyPreferences {
            return instance ?: synchronized(this) {
                instance ?: CurrencyPreferences(context).also { instance = it }
            }
        }
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveExchangeRate(baseCurrency: String, targetCurrency: String, rate: Double) {
        preferences.edit().apply {
            putString(KEY_BASE_CURRENCY, baseCurrency)
            putString(KEY_TARGET_CURRENCY, targetCurrency)
            putFloat(KEY_EXCHANGE_RATE, rate.toFloat())
            apply()
        }
    }

    fun getExchangeRate(baseCurrency: String, targetCurrency: String): Double? {
        val savedBaseCurrency = preferences.getString(KEY_BASE_CURRENCY, "EGP")
        val savedTargetCurrency = preferences.getString(KEY_TARGET_CURRENCY, "EGP")

        return if (savedBaseCurrency == baseCurrency && savedTargetCurrency == targetCurrency) {
            saveExchangeRate(savedBaseCurrency, savedTargetCurrency, preferences.getFloat(KEY_EXCHANGE_RATE, -1f).toDouble())
            preferences.getFloat(KEY_EXCHANGE_RATE, -1f).takeIf { it != -1f }?.toDouble()
        } else {
            null
        }
    }

    fun changeTargetCurrency(newTargetCurrency: String) {
        preferences.edit().apply {
            putString(KEY_TARGET_CURRENCY, newTargetCurrency)
            apply()
        }
    }

    fun getTargetCurrency() : String? {
        return preferences.getString(KEY_TARGET_CURRENCY, "EGP")
    }

    fun calculateTargetAmount(baseAmount: Double, targetCurrency: String): String? {
        if(targetCurrency == "EGP"){
            return String.format("%.2f %s", baseAmount, targetCurrency)
        } else {
            val rate =
                getExchangeRate("EGP", preferences.getString(KEY_TARGET_CURRENCY, null) ?: "EGP")
            return if (rate != null) {
                val targetAmount = baseAmount * rate
                String.format("%.2f %s", targetAmount, targetCurrency)
            } else {
                null
            }
        }
    }
}
