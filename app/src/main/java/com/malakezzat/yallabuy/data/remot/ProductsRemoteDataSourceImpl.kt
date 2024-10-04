package com.malakezzat.yallabuy.data.remot

import android.util.Log
import com.malakezzat.yallabuy.model.Category
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

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
        val response = productService.getCategories().customCollections
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Categories", e)
        emit(emptyList())
    }
}
