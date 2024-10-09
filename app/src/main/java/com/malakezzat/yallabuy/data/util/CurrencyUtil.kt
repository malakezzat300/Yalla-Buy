package com.malakezzat.yallabuy.data.util

import android.content.Context
import com.malakezzat.yallabuy.data.sharedpref.CurrencyPreferences

object CurrencyConverter {
    private var appContext: Context? = null

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    fun changeCurrency(amount: Double): String? {
        val targetCurrency = appContext?.let { CurrencyPreferences.getInstance(it).getTargetCurrency() }
        return targetCurrency?.let {
            CurrencyPreferences.getInstance(appContext!!).calculateTargetAmount(amount, it)
        }
    }

    fun getCurrency(): String? {
        return CurrencyPreferences.getInstance(appContext!!).getTargetCurrency()
    }
}