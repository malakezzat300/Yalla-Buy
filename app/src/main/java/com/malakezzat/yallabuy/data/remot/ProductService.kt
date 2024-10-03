package com.malakezzat.yallabuy.data.remot

import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.ProductsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductService {

    @GET("products.json")
    suspend fun getAllProducts(): ProductsResponse

    @POST("products.json")
    suspend fun createProduct(@Body product: ProductResponse): ProductResponse

}