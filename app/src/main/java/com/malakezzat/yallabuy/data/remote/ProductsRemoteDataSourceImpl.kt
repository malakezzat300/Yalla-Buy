package com.malakezzat.yallabuy.data.remote

import android.util.Log
import com.malakezzat.yallabuy.data.remot.ProductService
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.ProductResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProductsRemoteDataSourceImpl (var productService: ProductService):
    ProductsRemoteDataSource {

    private val TAG = "ProductsRemoteDataSource"
    companion object {
        private var instance: ProductsRemoteDataSourceImpl? = null
        fun getInstance(productService: ProductService): ProductsRemoteDataSourceImpl {
            return instance ?: synchronized(this) {
                val temp = ProductsRemoteDataSourceImpl(productService)
                instance = temp
                temp
            }
        }
}

    override suspend fun getAllProducts(): Flow<List<Product>>  = flow {
        val response = productService.getAllProducts().products
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching products", e)
        emit(emptyList())
    }

    override suspend fun getCategories(): Flow<List<CustomCollection>> = flow {
        val response = productService.getCategories().custom_collections
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Categories", e)
        throw e
    }

    override suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse> = flow {
        val response = productService.getAllDraftOrders()
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Categories", e)
        throw e
    }

    override suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> = flow {
        val response = productService.getDraftOrder(draftOrderId)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Categories", e)
        throw e
    }

    override suspend fun createDraftOrder(draftOrder: DraftOrder): Flow<DraftOrderResponse> = flow {
        val response = productService.createDraftOrder(draftOrder)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Categories", e)
        throw e
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrder: DraftOrder
    ): Flow<DraftOrderResponse> = flow {
        val response = productService.updateDraftOrder(draftOrderId,draftOrder)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Categories", e)
        throw e
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long) {
        productService.deleteDraftOrder(draftOrderId)
    }

    override suspend fun finalizeDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> = flow {
        val response = productService.finalizeDraftOrder(draftOrderId)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Categories", e)
        throw e
    }
    override suspend fun getProductById(id : Long) : Flow<ProductResponse> = flow{
        val response = productService.getProductById(id)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Product by id", e)
        throw e
    }

    override suspend fun createCustomer(customerRequest: CustomerRequest) : Flow<CustomerResponse> = flow{
        val response = productService.createCustomer(customerRequest)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error creating customer ", e)
        throw e
    }

    override suspend fun getCustomerByEmai(customer: String) : Flow<CustomerResponse> = flow{
        val response = productService.getCustomerByEmail(customer)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error creating customer ", e)
        throw e
    }
}
