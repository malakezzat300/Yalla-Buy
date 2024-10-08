package com.malakezzat.yallabuy.data.remote

import com.malakezzat.yallabuy.model.Address
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.CustomerAddress
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Order
import com.malakezzat.yallabuy.model.Orders
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.SmartCollection
import com.malakezzat.yallabuy.model.VariantResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductsRemoteDataSource {
    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductsByCollectionId(id:Long): Flow<List<Product>>
    suspend fun getCategories(): Flow<List<CustomCollection>>
    suspend fun getBrands(): Flow<List<SmartCollection>>
    suspend fun getAllOrdersForCustomerByID(id: Long): Flow<List<Order>>
    suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse>
    suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse>
    suspend fun createDraftOrder(draftOrder: DraftOrderRequest): Flow<DraftOrderResponse>
    suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrder: DraftOrderRequest
    ): Flow<DraftOrderResponse>

    suspend fun deleteDraftOrder(draftOrderId: Long)
    suspend fun finalizeDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse>
    suspend fun getProductById(id: Long) : Flow<ProductResponse>
    suspend fun createCustomer(customerRequest: CustomerRequest): Flow<CustomerResponse>
    suspend fun getCustomerByEmai(customer: String): Flow<CustomerSearchRespnse>
    suspend fun getCustomerById(customerId: Long): Flow<CustomerSearchRespnse>
    suspend fun getVariantById(variantId: Long): Flow<VariantResponse>
    suspend fun getConversionRate(): Flow<CurrencyResponse>

    suspend fun addNewAddress(customerId: Long,address: AddressRequest): Flow<CustomerAddress>
    suspend fun getUserAddresses(customerId: Long): Flow<List<Address>>
    suspend fun getAddressDetails(customerId: Long,addressId: Long): Flow<CustomerAddress>
    suspend fun updateUserAddress(customerId: Long,addressId: Long,address: AddressRequest): Flow<CustomerAddress>
    suspend fun setDefaultAddress(customerId: Long,addressId: Long): Flow<CustomerAddress>
    suspend fun deleteAddress(customerId: Long,addressId: Long)
}