package com.example.yallabuyadmin.coupons.model

import com.malakezzat.yallabuy.data.remot.ProductService
import com.malakezzat.yallabuy.data.remote.coupons.DiscountCode
import com.malakezzat.yallabuy.data.remote.coupons.PriceRule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CouponsRemoteDataSource(private val apiService: ProductService) {

    fun getPriceRules(): Flow<List<PriceRule>> = flow {
        try {
            val response = apiService.getPriceRules()
            emit(response.price_rules)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    fun getDiscountCodes(priceRuleId: Long): Flow<List<DiscountCode>> = flow {
        try {
            val response = apiService.getDiscountCodes(priceRuleId)
            emit(response.discount_codes)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

}
