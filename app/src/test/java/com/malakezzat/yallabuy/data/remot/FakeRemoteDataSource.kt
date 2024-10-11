package com.malakezzat.yallabuy.data.remot

import com.malakezzat.yallabuy.data.remote.ProductsRemoteDataSource
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.AddressResponse
import com.malakezzat.yallabuy.model.AppliedDiscount
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.Customer
import com.malakezzat.yallabuy.model.CustomerAddress
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DiscountCodeResponse
import com.malakezzat.yallabuy.model.DiscountCodesResponse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.Order
import com.malakezzat.yallabuy.model.PriceRuleResponse
import com.malakezzat.yallabuy.model.PriceRulesResponse
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.Property
import com.malakezzat.yallabuy.model.SmartCollection
import com.malakezzat.yallabuy.model.VariantResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource(
    private var products: List<Product>? = null,
    private var productsByCollection: List<Product>? = null,
    private var categories: List<CustomCollection>? = null,
    private var brands: List<SmartCollection>? = null
): ProductsRemoteDataSource {

    val draftOrdersList = listOf(
        DraftOrder(
            id = 1,
            note = "First draft order",
            line_items = listOf(
                LineItem(
                    title = "Cool T-Shirt",
                    price = "25.00",
                    variant_id = 101,
                    quantity = 2,
                    properties = listOf(
                        Property(name = "Size", value = "M"),
                        Property(name = "Color", value = "Blue")
                    ),
                    product_id = 201
                ),
                LineItem(
                    title = "Stylish Hat",
                    price = "15.00",
                    variant_id = 102,
                    quantity = 1,
                    properties = emptyList(),
                    product_id = 202
                )
            ),
            email = "customer@example.com",
            customer = Customer(id = 1),
            total_tax = "4.00",
            total_price = "64.00",
            subtotal_price = "60.00",
            applied_discount = AppliedDiscount(
                title = "Summer Sale",
                description = "10% off summer items",
                value = "10",
                value_type = "percentage",
                amount = "6.00"
            )
        ),
        DraftOrder(
            id = 2,
            note = "Second draft order",
            line_items = listOf(
                LineItem(
                    title = "Running Shoes",
                    price = "50.00",
                    variant_id = 201,
                    quantity = 1,
                    properties = listOf(
                        Property(name = "Size", value = "10")
                    ),
                    product_id = 301
                )
            ),
            email = "anothercustomer@example.com",
            customer = Customer(id = 2),
            total_tax = "2.00",
            total_price = "52.00",
            subtotal_price = "50.00",
            applied_discount = null // No discount applied
            )
    )

    override suspend fun getAllProducts(): Flow<List<Product>> {
        return flow {
            products?.let {
                emit(it)
            } ?: emit(emptyList())
        }.catch { e ->
            emit(emptyList())
        }
    }

    override suspend fun getProductsByCollectionId(id: Long): Flow<List<Product>> {
        return flow {
            productsByCollection?.let {
                emit(it)
            } ?: emit(emptyList())
        }.catch { e ->
            emit(emptyList())
        }
    }

    override suspend fun getCategories(): Flow<List<CustomCollection>> {
        return flow {
            categories?.let {
                emit(it)
            } ?: emit(emptyList())
        }.catch { e ->
            emit(emptyList())
        }
    }

    override suspend fun getBrands(): Flow<List<SmartCollection>> {
        return flow {
            brands?.let {
                emit(it)
            } ?: emit(emptyList())
        }.catch { e ->
            emit(emptyList())
        }
    }

    override suspend fun getAllOrdersForCustomerByID(id: Long): Flow<List<Order>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse> {
        return flow {
            emit(DraftOrdersResponse(draftOrdersList))
        }.catch { emit(DraftOrdersResponse(draftOrdersList)) }
    }

    override suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> {
        return flow {
            emit(DraftOrderResponse(draftOrdersList.find { it.id == draftOrderId} ?: DraftOrder()))
        }.catch { emit(DraftOrderResponse(DraftOrder(id = draftOrderId)))}
    }

    override suspend fun createDraftOrder(draftOrder: DraftOrderRequest): Flow<DraftOrderResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrder: DraftOrderRequest
    ): Flow<DraftOrderResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun finalizeDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductById(id: Long): Flow<ProductResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomer(customerRequest: CustomerRequest): Flow<CustomerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerByEmai(customer: String): Flow<CustomerSearchRespnse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerById(customerId: Long): Flow<CustomerSearchRespnse> {
        TODO("Not yet implemented")
    }

    override suspend fun getVariantById(variantId: Long): Flow<VariantResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getConversionRate(): Flow<CurrencyResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewAddress(
        customerId: Long,
        address: AddressRequest
    ): Flow<CustomerAddress> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserAddresses(customerId: Long): Flow<AddressResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getAddressDetails(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddress> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserAddress(
        customerId: Long,
        addressId: Long,
        address: AddressRequest
    ): Flow<CustomerAddress> {
        TODO("Not yet implemented")
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddress> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAddress(customerId: Long, addressId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getPriceRules(): Flow<PriceRulesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSinglePriceRule(priceRuleId: Long): Flow<PriceRuleResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getDiscountCodes(priceRuleId: Long): Flow<DiscountCodesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleDiscountCodes(
        priceRuleId: Long,
        discountCodeId: Long
    ): Flow<DiscountCodeResponse> {
        TODO("Not yet implemented")
    }
}