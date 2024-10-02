package com.malakezzat.yallabuy.data.remot

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    //const val baseUrl = "https://howtodoandroid.com/"
    const val BASE_URL = " "
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}