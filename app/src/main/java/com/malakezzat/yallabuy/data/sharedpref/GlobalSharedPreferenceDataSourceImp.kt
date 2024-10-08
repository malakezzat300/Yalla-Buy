package com.malakezzat.yallabuy.data.sharedpref

import android.content.SharedPreferences

class GlobalSharedPreferenceDataSourceImp(private val sharedPreferences: SharedPreferences):GlobalSharedPreferenceDataSource {
    private val userIdKey = "USER_ID"
    private val userEmailKey = "USER_EMAIL"

    override fun getUserId(): Long {
        return sharedPreferences.getLong(userIdKey, 0L)
    }

    override fun setUserId(id: Long) {
        sharedPreferences.edit().putLong(userIdKey, id).apply()
    }

    override fun getUserEmail(): String {
        return sharedPreferences.getString(userEmailKey,"empty")?:"empty"
    }
    override fun setUserEmail(string: String) {
        sharedPreferences.edit().putString(userEmailKey, string).apply()
    }

}