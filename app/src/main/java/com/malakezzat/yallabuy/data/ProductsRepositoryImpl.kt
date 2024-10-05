package com.malakezzat.yallabuy.data

import com.malakezzat.yallabuy.data.local.ProductsLocalDataSource
import com.malakezzat.yallabuy.data.remot.ProductsRemoteDataSource
import com.malakezzat.yallabuy.data.sharedpref.GlobalSharedPreferenceDataSource
import com.malakezzat.yallabuy.model.Category
import com.malakezzat.yallabuy.model.CustomCollection
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
}