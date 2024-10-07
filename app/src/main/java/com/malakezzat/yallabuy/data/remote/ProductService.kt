package com.malakezzat.yallabuy.data.remot


import com.malakezzat.yallabuy.data.remote.coupons.DiscountCodeResponse
import com.malakezzat.yallabuy.data.remote.coupons.priceRuleResponse
import com.malakezzat.yallabuy.model.Category
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.ProductsResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("draft_orders.json")
    suspend fun getAllDraftOrders(): DraftOrdersResponse

    @GET("draft_orders/{draft_order_id}.json")
    suspend fun getDraftOrder(@Path("draft_order_id") draftOrderId: Long): DraftOrderResponse

    @POST("draft_orders.json")
    suspend fun createDraftOrder(@Body draftOrder: DraftOrder): DraftOrderResponse

    @PUT("draft_orders/{draft_order_id}.json")
    suspend fun updateDraftOrder(
        @Path("draft_order_id") draftOrderId: Long,
        @Body draftOrder: DraftOrder
    ): DraftOrderResponse

    @DELETE("draft_orders/{draft_order_id}.json")
    suspend fun deleteDraftOrder(@Path("draft_order_id") draftOrderId: Long)

    @POST("draft_orders/{draft_order_id}/complete.json")
    suspend fun finalizeDraftOrder(@Path("draft_order_id") draftOrderId: Long): DraftOrderResponse

    @GET("products/{id}.json")
    suspend fun getProductById(@Path("id") productId: Long): ProductResponse

    @POST("customers.json")
    suspend fun createCustomer(
        @Body customerRequest: CustomerRequest
    ): CustomerResponse

    @GET("customers/search.json")
    suspend fun getCustomerByEmail(
        @Query("email") email: String
    ): CustomerSearchRespnse

    @GET("customers/search.json")
    suspend fun getCustomerById(
        @Query("id") email: Long
    ): CustomerSearchRespnse
}