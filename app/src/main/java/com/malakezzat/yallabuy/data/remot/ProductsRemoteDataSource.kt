package com.malakezzat.yallabuy.data.remot

import com.malakezzat.yallabuy.model.Category
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRemoteDataSource {
            suspend fun getAllProducts(): Flow<List<Product>>

            suspend fun getCategories():Flow<List<CustomCollection>>
}