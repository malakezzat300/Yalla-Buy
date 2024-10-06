package com.malakezzat.yallabuy.data

import com.malakezzat.yallabuy.data.local.ProductsLocalDataSource
import com.malakezzat.yallabuy.data.remot.ProductsRemoteDataSource
import com.malakezzat.yallabuy.data.sharedpref.GlobalSharedPreferenceDataSource
import com.malakezzat.yallabuy.model.Category
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.Flow

class ProductsRepositoryImpl private constructor(
    private var productsRemoteDataSource: ProductsRemoteDataSource,
    private var globalSharedPreferenceDataSource: GlobalSharedPreferenceDataSource
    ):ProductsRepository{

    companion object{
        private var instance: ProductsRepositoryImpl? = null
        fun getInstance( productsRemoteDataSource: ProductsRemoteDataSource,
                         globalSharedPreferenceDataSource: GlobalSharedPreferenceDataSource
                        ):ProductsRepositoryImpl{
            return instance ?: synchronized(this){
                val temp =ProductsRepositoryImpl(productsRemoteDataSource,
                    globalSharedPreferenceDataSource)
                instance = temp
                temp

        }
    }
}

    override suspend fun getAllProducts(): Flow<List<Product>> {
        return productsRemoteDataSource.getAllProducts()
    }

    override suspend fun getCategories(): Flow<List<CustomCollection>> {
        return productsRemoteDataSource.getCategories()
    }

    override suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse> {
        return productsRemoteDataSource.getAllDraftOrders()
    }

    override suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> {
        return productsRemoteDataSource.getDraftOrder(draftOrderId)
    }

    override suspend fun createDraftOrder(draftOrder: DraftOrder): Flow<DraftOrderResponse> {
        return productsRemoteDataSource.createDraftOrder(draftOrder)
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrder: DraftOrder
    ): Flow<DraftOrderResponse> {
        return productsRemoteDataSource.updateDraftOrder(draftOrderId,draftOrder)
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long) {
        return productsRemoteDataSource.deleteDraftOrder(draftOrderId)
    }

    override suspend fun finalizeDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> {
        return finalizeDraftOrder(draftOrderId)
    }
}