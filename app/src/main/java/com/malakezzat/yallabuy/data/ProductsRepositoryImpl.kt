package com.malakezzat.yallabuy.data

import com.malakezzat.yallabuy.data.remote.ProductsRemoteDataSource
import com.malakezzat.yallabuy.data.sharedpref.GlobalSharedPreferenceDataSource
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.AddressResponse
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.CustomerAddress
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DiscountCodesResponse
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Order
import com.malakezzat.yallabuy.model.PriceRuleResponse
import com.malakezzat.yallabuy.model.PriceRulesResponse
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.DiscountCodeResponse
import com.malakezzat.yallabuy.model.SmartCollection
import com.malakezzat.yallabuy.model.VariantResponse
import kotlinx.coroutines.flow.Flow

class ProductsRepositoryImpl private constructor(
    private var productsRemoteDataSource: ProductsRemoteDataSource,
    private var globalSharedPreferenceDataSource: GlobalSharedPreferenceDataSource
    ):ProductsRepository{

    companion object{
        private var instance: ProductsRepositoryImpl? = null
        fun getInstance( productsRemoteDataSource: ProductsRemoteDataSource,
                         globalSharedPreferenceDataSource: GlobalSharedPreferenceDataSource)
        :ProductsRepositoryImpl{
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

    override suspend fun getProductsByCollectionId(id: Long): Flow<List<Product>> {
        return productsRemoteDataSource.getProductsByCollectionId(id)
    }

    override suspend fun getCategories(): Flow<List<CustomCollection>> {
        return productsRemoteDataSource.getCategories()
    }

    override suspend fun getBrands(): Flow<List<SmartCollection>> {
        return productsRemoteDataSource.getBrands()
    }

    override suspend fun getAllOrdersForCustomerByID(id: Long): Flow<List<Order>> {
        return productsRemoteDataSource.getAllOrdersForCustomerByID(id)
    }

    override suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse> {
        return productsRemoteDataSource.getAllDraftOrders()
    }

    override suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> {
        return productsRemoteDataSource.getDraftOrder(draftOrderId)
    }

    override suspend fun createDraftOrder(draftOrder: DraftOrderRequest): Flow<DraftOrderResponse> {
        return productsRemoteDataSource.createDraftOrder(draftOrder)
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrder: DraftOrderRequest
    ): Flow<DraftOrderResponse> {
        return productsRemoteDataSource.updateDraftOrder(draftOrderId,draftOrder)
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long) {
        return productsRemoteDataSource.deleteDraftOrder(draftOrderId)
    }

    override suspend fun finalizeDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> {
        return productsRemoteDataSource.finalizeDraftOrder(draftOrderId)
    }

    override suspend fun getProductById(productId: Long): Flow<ProductResponse> {
        return productsRemoteDataSource.getProductById(productId)
    }
    override suspend fun createCustomer(customerRequest: CustomerRequest): Flow<CustomerResponse> {
        return productsRemoteDataSource.createCustomer(customerRequest)
    }
    override suspend fun getCustomerByEmail(customer: String): Flow<CustomerSearchRespnse> {
        return productsRemoteDataSource.getCustomerByEmai(customer)
    }
    override suspend fun getCustomerById(customer: Long): Flow<CustomerSearchRespnse> {
        return productsRemoteDataSource.getCustomerById(customer)
    }

    override suspend fun getVariantById(variantId: Long): Flow<VariantResponse> {
        return productsRemoteDataSource.getVariantById(variantId)
    }

    override suspend fun getConversionRate(): Flow<CurrencyResponse> {
        return productsRemoteDataSource.getConversionRate()
    }


    override suspend fun addNewAddress(customerId: Long,address: AddressRequest): Flow<CustomerAddress> {
        return productsRemoteDataSource.addNewAddress(customerId,address)
    }

    override suspend fun getUserAddresses(customerId: Long): Flow<AddressResponse> {
        return productsRemoteDataSource.getUserAddresses(customerId)
    }

    override suspend fun getAddressDetails(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddress> {
        return productsRemoteDataSource.getAddressDetails(customerId,addressId)
    }

    override suspend fun updateUserAddress(
        customerId: Long,
        addressId: Long,
        address: AddressRequest,
    ): Flow<CustomerAddress> {
        return productsRemoteDataSource.updateUserAddress(customerId,addressId,address)
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddress> {
        return productsRemoteDataSource.setDefaultAddress(customerId,addressId)
    }

    override suspend fun deleteAddress(customerId: Long, addressId: Long) {
        productsRemoteDataSource.deleteAddress(customerId, addressId)
    }

    override fun getUserId(): Long {
        return globalSharedPreferenceDataSource.getUserId()
    }

    override fun setUserId(id: Long) {
        globalSharedPreferenceDataSource.setUserId(id)
    }

    override fun getUserEmail(): String {
        return globalSharedPreferenceDataSource.getUserEmail()
    }

    override fun setUserEmail(string: String) {
        globalSharedPreferenceDataSource.setUserEmail(string)

    }

    override suspend fun getPriceRules(): Flow<PriceRulesResponse> {
        return productsRemoteDataSource.getPriceRules()
    }

    override suspend fun getSinglePriceRule(priceRuleId: Long): Flow<PriceRuleResponse> {
        return productsRemoteDataSource.getSinglePriceRule(priceRuleId)
    }

    override suspend fun getDiscountCodes(priceRuleId: Long): Flow<DiscountCodesResponse> {
        return productsRemoteDataSource.getDiscountCodes(priceRuleId)
    }

    override suspend fun getSingleDiscountCodes(
        priceRuleId: Long,
        discountCodeId: Long
    ): Flow<DiscountCodeResponse> {
        return productsRemoteDataSource.getSingleDiscountCodes(priceRuleId, discountCodeId)
    }


}