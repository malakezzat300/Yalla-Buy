package com.malakezzat.yallabuy.data.remote

import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.SmartCollection
import kotlinx.coroutines.flow.Flow

interface ProductsRemoteDataSource {
    suspend fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductsByCollectionId(id:Long): Flow<List<Product>>
    suspend fun getCategories(): Flow<List<CustomCollection>>
    suspend fun getBrands(): Flow<List<SmartCollection>>

    suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse>
    suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse>
    suspend fun createDraftOrder(draftOrder: DraftOrder): Flow<DraftOrderResponse>
    suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrder: DraftOrder
    ): Flow<DraftOrderResponse>

    suspend fun deleteDraftOrder(draftOrderId: Long)
    suspend fun finalizeDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse>
    suspend fun getProductById(id: Long) : Flow<ProductResponse>
    suspend fun createCustomer(customerRequest: CustomerRequest): Flow<CustomerResponse>
    suspend fun getCustomerByEmai(customer: String): Flow<CustomerSearchRespnse>
    suspend fun getCustomerById(customerId: Long): Flow<CustomerSearchRespnse>

}