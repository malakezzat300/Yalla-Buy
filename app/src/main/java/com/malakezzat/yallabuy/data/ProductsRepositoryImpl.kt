package com.malakezzat.yallabuy.data

import com.malakezzat.yallabuy.data.local.ProductsLocalDataSource
import com.malakezzat.yallabuy.data.remot.ProductsRemoteDataSource

class ProductsRepositoryImpl private constructor(
    private var productsLocalDataSource: ProductsLocalDataSource,
    private var productsRemoteDataSource: ProductsRemoteDataSource
    ):ProductsRepository{

    companion object{
        private var instance: ProductsRepositoryImpl? = null
        fun getInstance(productsLocalDataSource: ProductsLocalDataSource,
                        productsRemoteDataSource: ProductsRemoteDataSource
                        ):ProductsRepositoryImpl{
            return instance ?: synchronized(this){
                val temp =ProductsRepositoryImpl(
                    productsLocalDataSource,productsRemoteDataSource)
                instance = temp
                temp

        }
    }
}
    }