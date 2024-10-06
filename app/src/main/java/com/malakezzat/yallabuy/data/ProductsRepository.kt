package com.malakezzat.yallabuy.data

import com.malakezzat.yallabuy.data.remot.coupons.DiscountCode
import com.malakezzat.yallabuy.data.remot.coupons.DiscountCodeResponse
import com.malakezzat.yallabuy.data.remot.coupons.PriceRule
import com.malakezzat.yallabuy.data.remot.coupons.priceRuleResponse
import com.malakezzat.yallabuy.model.Category
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    suspend fun getAllProducts(): Flow<List<Product>>

    suspend fun getCategories(): Flow<List<CustomCollection>>


    fun getPriceRules(): Flow<List<PriceRule>>
    fun getDiscountCodes(priceRuleId: Long): Flow<List<DiscountCode>>
    suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse>
    suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse>
    suspend fun createDraftOrder(draftOrder: DraftOrder): Flow<DraftOrderResponse>
    suspend fun updateDraftOrder(draftOrderId: Long,draftOrder: DraftOrder): Flow<DraftOrderResponse>
    suspend fun deleteDraftOrder(draftOrderId: Long)
    suspend fun finalizeDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse>

}