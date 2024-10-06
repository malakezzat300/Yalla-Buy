package com.malakezzat.yallabuy.data.remote

import android.util.Log
import com.malakezzat.yallabuy.data.remot.ProductService
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.SmartCollection
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

    override suspend fun getProductsByCategory(id: Long): Flow<List<Product>> = flow  {
        val response = productService.getProductsInCollection(id).products
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

    override suspend fun getBrands(): Flow<List<SmartCollection>> = flow{
        val response = productService.getBrands().smart_collections
        emit(response)
    }.catch {e->
        Log.e(TAG, "Error fetching Brands", e)
        throw e
    }

    override suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse> = flow {
        val response = productService.getAllDraftOrders()
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching AllDraftOrders", e)
        throw e
    }

    override suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> = flow {
        val response = productService.getDraftOrder(draftOrderId)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching DraftOrder", e)
        throw e
    }

    override suspend fun createDraftOrder(draftOrder: DraftOrder): Flow<DraftOrderResponse> = flow {
        val response = productService.createDraftOrder(draftOrder)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error create DraftOrder", e)
        throw e
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrder: DraftOrder
    ): Flow<DraftOrderResponse> = flow {
        val response = productService.updateDraftOrder(draftOrderId,draftOrder)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error update DraftOrder", e)
        throw e
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long) {
        productService.deleteDraftOrder(draftOrderId)
    }

    override suspend fun finalizeDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> = flow {
        val response = productService.finalizeDraftOrder(draftOrderId)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error finalize DraftOrder", e)
        throw e
    }
    override suspend fun getProductById(id : Long) : Flow<ProductResponse> = flow{
        val response = productService.getProductById(id)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Product by id", e)
        throw e
    }
}
