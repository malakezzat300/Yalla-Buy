package com.malakezzat.yallabuy.data.sharedpref

interface GlobalSharedPreferenceDataSource {
    //user id
    fun getUserId(): Long
    fun setUserId(id: Long)

    //user email
    fun getUserEmail():String
    fun setUserEmail(string:String)
}
