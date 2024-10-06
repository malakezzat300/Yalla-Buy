package com.malakezzat.yallabuy.data

import com.malakezzat.yallabuy.data.remote.coupons.DiscountCode
import com.malakezzat.yallabuy.data.remote.coupons.PriceRule
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getAllProducts(): Flow<List<Product>>

    suspend fun getCategories(): Flow<List<CustomCollection>>

    fun getPriceRules(): Flow<List<PriceRule>>
    fun getDiscountCodes(priceRuleId: Long): Flow<List<DiscountCode>>
}