package com.malakezzat.yallabuy.data.remote

import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", Credentials
                    .basic("6d9bf027d5ce002ce4f2697d43adccdf",
                        "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"))
                .build()
            chain.proceed(request)
        }
        .build()
    //const val baseUrl = "https://howtodoandroid.com/"
    const val BASE_URL = "https://android-sv24-r3team4.myshopify.com/admin/api/2024-07/"
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

}