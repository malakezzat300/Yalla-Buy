package com.malakezzat.yallabuy.data.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.malakezzat.yallabuy.model.ConversionRates

class CurrencyPreferences private constructor(context: Context) {

    companion object {
        private const val PREFS_NAME = "currency_prefs"
        private const val KEY_EXCHANGE_RATE = "exchange_rate"
        private const val KEY_EGP = "EGP"
        private const val KEY_USD = "USD"
        private const val KEY_EUR = "EUR"
        private const val KEY_AED = "AED"
        private const val KEY_SAR = "SAR"
        private const val KEY_CURRENCY = "Current"
        private const val FIRST_LAUNCH = "first"


        @Volatile
        private var instance: CurrencyPreferences? = null

        fun getInstance(context: Context): CurrencyPreferences {
            return instance ?: synchronized(this) {
                instance ?: CurrencyPreferences(context).also { instance = it }
            }
        }
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveExchangeRate(conversionRates :ConversionRates?) {
        preferences.edit().apply {
            putLong(KEY_EGP, conversionRates?.EGP ?: 1)
            putFloat(KEY_USD, conversionRates?.USD?.toFloat() ?: -1f)
            putFloat(KEY_EUR, conversionRates?.EUR?.toFloat() ?: -1f)
            putFloat(KEY_AED, conversionRates?.AED?.toFloat() ?: -1f)
            putFloat(KEY_SAR, conversionRates?.SAR?.toFloat() ?: -1f)
            apply()
        }
    }

    private fun getExchangeRate(targetCurrency: String) {
        val savedUSD = preferences.getFloat(KEY_USD, -1f).toDouble()
        val savedEUR = preferences.getFloat(KEY_EUR, -1f).toDouble()
        val savedAED = preferences.getFloat(KEY_AED, -1f).toDouble()
        val savedSAR = preferences.getFloat(KEY_SAR, -1f).toDouble()

        return when (targetCurrency) {
            "USD" -> preferences.edit().putFloat(KEY_EXCHANGE_RATE,savedUSD.toFloat()).apply()
            "EUR" -> preferences.edit().putFloat(KEY_EXCHANGE_RATE,savedEUR.toFloat()).apply()
            "AED" -> preferences.edit().putFloat(KEY_EXCHANGE_RATE,savedAED.toFloat()).apply()
            "SAR" -> preferences.edit().putFloat(KEY_EXCHANGE_RATE,savedSAR.toFloat()).apply()
            else -> {}
        }
    }

    fun changeTargetCurrency(newTargetCurrency: String) {
        preferences.edit().apply {
            putString(KEY_CURRENCY, newTargetCurrency)
            apply()
        }
        getExchangeRate(newTargetCurrency)
    }

    fun getTargetCurrency() : String? {
        return preferences.getString(KEY_CURRENCY, "EGP")
    }

    fun calculateTargetAmount(baseAmount: Double, targetCurrency: String): String {
        if(targetCurrency == "EGP"){
            return String.format("%.2f %s", baseAmount, targetCurrency)
        } else {
            val rate = preferences.getFloat(KEY_EXCHANGE_RATE,1f)
            return run {
                val targetAmount = baseAmount * rate
                String.format("%.2f %s", targetAmount, targetCurrency)
            }
        }
    }

    fun getFirstLaunch() : Boolean {
        return preferences.getBoolean(FIRST_LAUNCH,true)
    }

    fun setFirstLaunch(firstLaunch : Boolean){
        preferences.edit().apply {
            putBoolean(FIRST_LAUNCH, firstLaunch)
            apply()
        }
    }
}
