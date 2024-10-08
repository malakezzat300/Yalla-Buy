package com.malakezzat.yallabuy.data.remot


import com.malakezzat.yallabuy.model.DiscountCodesResponse
import com.malakezzat.yallabuy.model.DiscountCodeResponse
import com.malakezzat.yallabuy.model.PriceRulesResponse
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.AddressResponse
import com.malakezzat.yallabuy.model.Brands
import com.malakezzat.yallabuy.model.Category
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomerAddress
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Orders
import com.malakezzat.yallabuy.model.PriceRuleResponse
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.ProductsResponse
import com.malakezzat.yallabuy.model.VariantResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
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

    @GET("collections/{collection_id}/products.json") //collections/841564295/products.json
    suspend fun getProductsInCollection(
        @Path("collection_id") collectionId: Long
    ): ProductsResponse

    @GET("smart_collections.json")
    suspend fun getBrands(): Brands

    @GET("price_rules.json")
    suspend fun getPriceRules(): PriceRulesResponse

    @GET("price_rules/{price_rule_id}.json")
    suspend fun getSinglePriceRule(@Path("price_rule_id") priceRuleId: Long): PriceRuleResponse

    @GET("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun getDiscountCodes(
        @Path("price_rule_id") priceRuleId: Long
    ): DiscountCodesResponse

    @GET("price_rules/{price_rule_id}/discount_codes/{discount_code_id}.json")
    suspend fun getSingleDiscountCode(
        @Path("price_rule_id") priceRuleId: Long,
        @Path("discount_code_id") discountCodeId: Long
    ): DiscountCodeResponse

    @GET("draft_orders.json")
    suspend fun getAllDraftOrders(): DraftOrdersResponse

    @GET("draft_orders/{draft_order_id}.json")
    suspend fun getDraftOrder(@Path("draft_order_id") draftOrderId: Long): DraftOrderResponse

    @POST("draft_orders.json")
    suspend fun createDraftOrder(@Body draftOrder: DraftOrderRequest): DraftOrderResponse

    @PUT("draft_orders/{draft_order_id}.json")
    suspend fun updateDraftOrder(
        @Path("draft_order_id") draftOrderId: Long,
        @Body draftOrder: DraftOrderRequest
    ): DraftOrderResponse

    @DELETE("draft_orders/{draft_order_id}.json")
    suspend fun deleteDraftOrder(@Path("draft_order_id") draftOrderId: Long)

    @Headers("Accept: application/json")
    @PUT("draft_orders/{draft_order_id}/complete.json")
    suspend fun finalizeDraftOrder(@Path("draft_order_id") draftOrderId: Long): DraftOrderResponse

    @GET("products/{id}.json")
    suspend fun getProductById(@Path("id") productId: Long): ProductResponse

    @POST("customers.json")
    suspend fun createCustomer(@Body customerRequest: CustomerRequest): CustomerResponse

    @GET("customers/search.json")
    suspend fun getCustomerByEmail(@Query("email") email: String): CustomerSearchRespnse

    @GET("customers/search.json")
    suspend fun getCustomerById(@Query("id") email: Long): CustomerSearchRespnse

    @GET("variants/{id}.json")
    suspend fun getVariantById(@Path("id") variantId: Long): VariantResponse


    //customers/207119551/orders.json
    /*@GET("customers/{id}/orders.json")
    suspend fun getAllOrdersForCustomerById(
        @Path("id") productId: Long
    )*/

    // Function to retrieve all orders for a specific customer by their ID
    @GET("customers/{id}/orders.json")
    suspend fun getAllOrdersForCustomerByID(
        @Path("id") customerId: Long
    ): Orders // Ensure Orders is the appropriate type to represent the response

    @GET("latest/EGP")
    suspend fun getLatestRates(): CurrencyResponse

    @POST("customers/{customer_id}/addresses.json")
    suspend fun addNewAddress(@Path("customer_id") customerId: Long,
                              @Body address: AddressRequest): CustomerAddress

    @GET("customers/{customer_id}/addresses.json")
    suspend fun getUserAddresses(@Path("customer_id") customerId: Long): AddressResponse

    @GET("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun getAddressDetails(@Path("customer_id") customerId: Long,@Path("address_id") addressId: Long): CustomerAddress

    @PUT("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun updateUserAddress(@Path("customer_id") customerId: Long,@Path("address_id") addressId: Long,
                                  @Body address: AddressRequest): CustomerAddress

    @PUT("customers/{customer_id}/addresses/{address_id}/default.json")
    suspend fun setDefaultAddress(@Path("customer_id") customerId: Long,@Path("address_id") addressId: Long): CustomerAddress

    @DELETE("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun deleteAddress(@Path("customer_id") customerId: Long,@Path("address_id") addressId: Long)
}