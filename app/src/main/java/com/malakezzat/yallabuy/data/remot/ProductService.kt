package com.malakezzat.yallabuy.data.remot


import com.malakezzat.yallabuy.data.remot.coupons.DiscountCodeResponse
import com.malakezzat.yallabuy.data.remot.coupons.priceRuleResponse
import com.malakezzat.yallabuy.model.Category
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.ProductsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductService {

    @GET("products.json")
    suspend fun getAllProducts(): ProductsResponse

    @POST("products.json")
    suspend fun createProduct(@Body product: ProductResponse): ProductResponse

    @GET("custom_collections.json")
    suspend fun getCategories(): Category

    @GET("price_rules.json")
    suspend fun getPriceRules(): priceRuleResponse

    @GET("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun getDiscountCodes(
        @Path("price_rule_id") priceRuleId: Long
    ): DiscountCodeResponse
}