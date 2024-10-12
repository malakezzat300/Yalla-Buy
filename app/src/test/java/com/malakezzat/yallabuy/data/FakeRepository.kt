package com.malakezzat.yallabuy.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Address
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.AddressResponse
import com.malakezzat.yallabuy.model.AppliedDiscount
import com.malakezzat.yallabuy.model.ConversionRates
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.Customer
import com.malakezzat.yallabuy.model.CustomerAddress
import com.malakezzat.yallabuy.model.CustomerDetails
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DiscountCodeResponse
import com.malakezzat.yallabuy.model.DiscountCodesResponse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.ImageCategory
import com.malakezzat.yallabuy.model.LineItem
import com.malakezzat.yallabuy.model.Order
import com.malakezzat.yallabuy.model.PriceRule
import com.malakezzat.yallabuy.model.PriceRuleResponse
import com.malakezzat.yallabuy.model.PriceRulesResponse
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.SmartCollection
import com.malakezzat.yallabuy.model.VariantResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class FakeRepository : ProductsRepository {

    val lineItems1 = listOf(
        LineItem(
            title = "Product A",
            price = "100.0",
            variant_id = 1L,
            quantity = 2,
            properties = emptyList(),
            product_id = 101L
        ),
        LineItem(
            title = "Product B",
            price = "50.0",
            variant_id = 2L,
            quantity = 1,
            properties = emptyList(),
            product_id = 102L
        )
    )

    val lineItems2 = listOf(
        LineItem(
            title = "Product C",
            price = "200.0",
            variant_id = 3L,
            quantity = 1,
            properties = emptyList(),
            product_id = 103L
        )
    )

    val lineItems3 = listOf(
        LineItem(
            title = "Product D",
            price = "75.0",
            variant_id = 4L,
            quantity = 3,
            properties = emptyList(),
            product_id = 104L
        )
    )

    val draftOrder1 = DraftOrder(
        id = 1L,
        note = "First draft order",
        line_items = lineItems1,
        email = "customer1@example.com",
        customer = Customer(id = 1001L),
        total_tax = "15.0",
        total_price = "215.0",
        subtotal_price = "200.0",
        applied_discount = AppliedDiscount(
            title = "Summer Sale",
            description = "10% off",
            value = "10",
            value_type = "percentage",
            amount = "20.0"
        )
    )

    val draftOrder2 = DraftOrder(
        id = 2L,
        note = "Second draft order",
        line_items = lineItems2,
        email = "customer2@example.com",
        customer = Customer(id = 1002L),
        total_tax = "30.0",
        total_price = "230.0",
        subtotal_price = "200.0",
        applied_discount = AppliedDiscount(
            title = "Special Discount",
            description = "5% off",
            value = "5",
            value_type = "percentage",
            amount = "10.0"
        )
    )

    val draftOrder3 = DraftOrder(
        id = 3L,
        note = "Third draft order",
        line_items = lineItems3,
        email = "customer3@example.com",
        customer = Customer(id = 1003L),
        total_tax = "22.5",
        total_price = "247.5",
        subtotal_price = "225.0",
        applied_discount = AppliedDiscount(
            title = "Clearance Sale",
            description = "Flat 15 off",
            value = "15",
            value_type = "amount",
            amount = "15.0"
        )
    )

    val draftOrdersResponse = DraftOrdersResponse(
        draft_orders = listOf(draftOrder1, draftOrder2, draftOrder3)
    )

    val conversionRates = ConversionRates(
        EGP = 1L,
        USD = 0.02,
        AED = 0.075,
        SAR = 0.077,
        EUR = 0.018
    )

    val currencyResponse = CurrencyResponse(
        result = "success",
        documentation = "https://www.exchangerate-api.com/docs",
        terms_of_use = "https://www.exchangerate-api.com/terms",
        time_last_update_unix = 1638477600L,
        time_last_update_utc = "2024-10-08 12:00 UTC",
        time_next_update_unix = 1638564000L,
        time_next_update_utc = "2024-10-09 12:00 UTC",
        base_code = "EGP",
        conversion_rates = conversionRates
    )

    val priceRule1 = PriceRule(
        id = 1L,
        title = "Holiday Discount",
        target_type = "line_item",
        value_type = "percentage",
        value = -10.0,
        starts_at = "2024-12-01T00:00:00Z",
        ends_at = "2024-12-31T23:59:59Z",
        target_selection = "all",
        allocation_method = "across",
        customer_selection = "all",
        usage_limit = 100L
    )

    val priceRule2 = PriceRule(
        id = 2L,
        title = "New Year Sale",
        target_type = "line_item",
        value_type = "fixed_amount",
        value = -15.0,
        starts_at = "2024-12-31T00:00:00Z",
        ends_at = "2025-01-01T23:59:59Z",
        target_selection = "all",
        allocation_method = "each",
        customer_selection = "all",
        usage_limit = 50L
    )

    val priceRule3 = PriceRule(
        id = 3L,
        title = "VIP Discount",
        target_type = "shipping_line",
        value_type = "percentage",
        value = -20.0,
        starts_at = "2024-11-01T00:00:00Z",
        ends_at = "2024-11-30T23:59:59Z",
        target_selection = "all",
        allocation_method = "each",
        customer_selection = "specific",
        usage_limit = 10L
    )

    val priceRulesResponse = PriceRulesResponse(
        price_rules = listOf(priceRule1, priceRule2, priceRule3)
    )

    val priceRuleResponse = PriceRuleResponse(
        price_rule = priceRule1
    )

    val address1 = Address(
        id = 1,
        customer_id = 101,
        first_name = "John",
        last_name = "Doe",
        company = "Doe Enterprises",
        address1 = "123 Main St",
        address2 = "Apt 4B",
        city = "Springfield",
        province = "Illinois",
        country = "USA",
        zip = "62701",
        phone = "555-1234",
        name = "John Doe",
        province_code = "IL",
        country_code = "US",
        country_name = "United States",
        default = true
    )

    val address2 = Address(
        id = 2,
        customer_id = 102,
        first_name = "Jane",
        last_name = "Smith",
        company = "Smith LLC",
        address1 = "456 Elm St",
        address2 = null, // Optional field
        city = "Los Angeles",
        province = "California",
        country = "USA",
        zip = "90001",
        phone = "555-5678",
        name = "Jane Smith",
        province_code = "CA",
        country_code = "US",
        country_name = "United States",
        default = false
    )

    val address3 = Address(
        id = 3,
        customer_id = 103,
        first_name = "Alice",
        last_name = "Johnson",
        company = "Johnson Corp",
        address1 = "789 Oak St",
        address2 = "Suite 100",
        city = "New York",
        province = "New York",
        country = "USA",
        zip = "10001",
        phone = "555-9012",
        name = "Alice Johnson",
        province_code = "NY",
        country_code = "US",
        country_name = "United States",
        default = true
    )

    val address33 = Address(
        id = 4,
        customer_id = 103,
        first_name = "Alice",
        last_name = "Johnson",
        company = "Johnson Corp",
        address1 = "143 Oak St",
        address2 = "Suite 123",
        city = "New York",
        province = "New York",
        country = "USA",
        zip = "10001",
        phone = "555-9012",
        name = "Alice Johnson",
        province_code = "NY",
        country_code = "US",
        country_name = "United States",
        default = false
    )

    val address333 = Address(
        id = 5,
        customer_id = 103,
        first_name = "Alice5",
        last_name = "Johnson5",
        company = "Johnson Corp5",
        address1 = "143 Oak St",
        address2 = "Suite 123",
        city = "New York",
        province = "New York",
        country = "USA",
        zip = "10001",
        phone = "555-9012",
        name = "Alice Johnson",
        province_code = "NY",
        country_code = "US",
        country_name = "United States",
        default = false
    )

    private val addresses = mutableListOf(address1,address2,address3,address33)

    val customerResponse = CustomerResponse(CustomerDetails(1L, "tastName@gmail.com", "testName","","",""))

    val emails = listOf("customer1@example.com","customer2@example.com","customer3@example.com",)

    override suspend fun getAllProducts(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun getProductsByCollectionId(id: Long): Flow<List<Product>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(): Flow<List<CustomCollection>> {
        val customCollections = listOf(
            CustomCollection(
                published_scope = "global",
                updated_at = "2024-10-11T12:00:00Z",
                admin_graphql_api_id = "gid://shopify/CustomCollection/1",
                handle = "collection-1",
                id = 1L,
                title = "Winter Collection",
                published_at = "2024-06-01T12:00:00Z",
                sort_order = "best-selling",
                image = ImageCategory(
                    src = "https://example.com/image1.jpg",
                    width = 800,
                    created_at = "2024-06-01T12:00:00Z",
                    height = 600
                ),
                body_html = "<p>Summer collection description</p>"
            ),
            CustomCollection(
                published_scope = "global",
                updated_at = "2024-10-11T12:00:00Z",
                admin_graphql_api_id = "gid://shopify/CustomCollection/2",
                handle = "collection-2",
                id = 2L,
                title = "Winter Collection",
                published_at = "2024-10-01T12:00:00Z",
                sort_order = "alphabetical",
                image = ImageCategory(
                    src = "https://example.com/image2.jpg",
                    width = 800,
                    created_at = "2024-10-01T12:00:00Z",
                    height = 600
                ),
                body_html = "<p>Winter collection description</p>"
            )
        )

        return flow {
            emit(customCollections)
        }.catch { e ->
            Log.e("FakeRepository", "Error fetching categories", e)
            emit(emptyList())
        }
    }


    override suspend fun getBrands(): Flow<List<SmartCollection>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllOrdersForCustomerByID(id: Long): Flow<List<Order>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse> {
        return flow {
            emit(draftOrdersResponse)
        }.catch { e ->
            Log.e("FakeRepository", "Error fetching categories", e)
            emit(DraftOrdersResponse(emptyList()))
        }
    }

    override suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> {
        TODO("Not yet implemented")
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

    override suspend fun getProductById(productId: Long): Flow<ProductResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createCustomer(customerRequest: CustomerRequest): Flow<CustomerResponse> {
        return flow {
           //delay(1000)
            emit(customerResponse)
        }
    }

    override suspend fun getCustomerByEmail(customer: String): Flow<CustomerSearchRespnse> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomerById(customer: Long): Flow<CustomerSearchRespnse> {
        TODO("Not yet implemented")
    }

    override suspend fun getVariantById(variantId: Long): Flow<VariantResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getConversionRate(): Flow<CurrencyResponse> {
        return flow {
            emit(currencyResponse)
        }
    }

    override suspend fun addNewAddress(
        customerId: Long,
        address: AddressRequest
    ): Flow<CustomerAddress> {
        return flow {
            val userAddresses = addresses.filter { it.customer_id == customerId }.toMutableList()
            userAddresses.add(address.address)
            emit(CustomerAddress(address.address))
        }
    }

    override suspend fun getUserAddresses(customerId: Long): Flow<AddressResponse> {
        return flow {
            val userAddresses = addresses.filter { it.customer_id == customerId }
            emit(AddressResponse(userAddresses))
        }
    }

    override suspend fun getAddressDetails(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddress> {
        return flow {
            emit(CustomerAddress(addresses[customerId.toInt()]))
        }
    }

    override suspend fun updateUserAddress(
        customerId: Long,
        addressId: Long,
        address: AddressRequest
    ): Flow<CustomerAddress> {
        return flow {
            val afterCustomerId = addresses.filter { it.customer_id == customerId }
            val afterAddressId = afterCustomerId.find { it.id == addressId }
            afterAddressId?.let { CustomerAddress(it) }?.let { emit(it) }
        }
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddress> {
        return flow {
            val index = addresses.indexOfFirst { it.id == addressId && it.customer_id == customerId }
            if (index != -1) {
                addresses.forEach { it.default = false }
                addresses[index].default = true
                emit(CustomerAddress(addresses[index]))
            } else {
                throw NoSuchElementException("Address not found to set as default")
            }
        }
    }

    override suspend fun deleteAddress(customerId: Long, addressId: Long) {
        addresses.removeIf { it.id == addressId && it.customer_id == customerId }
    }

    override fun getUserId(): Long {
        TODO("Not yet implemented")
    }

    override fun setUserId(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getUserEmail(): String {
        return  emails[0]
    }

    override fun setUserEmail(string: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getPriceRules(): Flow<PriceRulesResponse> = flow {
        ApiState.Success(priceRulesResponse)
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