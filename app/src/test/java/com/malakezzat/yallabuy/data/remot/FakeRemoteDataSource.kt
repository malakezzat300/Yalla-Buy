package com.malakezzat.yallabuy.data.remot

import com.malakezzat.yallabuy.data.remote.ProductsRemoteDataSource
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.AddressResponse
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.CustomerAddress
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DiscountCodeResponse
import com.malakezzat.yallabuy.model.DiscountCodesResponse
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Image
import com.malakezzat.yallabuy.model.ImageCategory
import com.malakezzat.yallabuy.model.Order
import com.malakezzat.yallabuy.model.PriceRuleResponse
import com.malakezzat.yallabuy.model.PriceRulesResponse
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.SmartCollection
import com.malakezzat.yallabuy.model.Variant
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
    private val productsList = listOf(
        Product(
            id = 1,
            title = "Cool T-Shirt",
            body_html = "<p>This is a cool t-shirt.</p>",
            vendor = "T-Shirt Co.",
            product_type = "Apparel",
            tags = "tshirt,cool,apparel",
            images = emptyList(),
            image = Image(0L,""),
            variants = emptyList(),
            options = emptyList()
        ),
        Product(
            id = 2,
            title = "Cool T-Shirt",
            body_html = "<p>This is a cool t-shirt.</p>",
            vendor = "T-Shirt Co.",
            product_type = "Apparel",
            tags = "tshirt,cool,apparel",
            images = emptyList(),
            image = Image(0L,""),
            variants = emptyList(),
            options = emptyList()
        ),
        Product(
            id = 3,
            title = "Cool T-Shirt",
            body_html = "<p>This is a cool t-shirt.</p>",
            vendor = "T-Shirt Co.",
            product_type = "Apparel",
            tags = "tshirt,cool,apparel",
            images = emptyList(),
            image = Image(0L,""),
            variants = emptyList(),
            options = emptyList()
        )
    )

    private val customCollectionsList = listOf(
        CustomCollection(
            published_scope = "global",
            updated_at = "2023-10-01T12:00:00Z",
            admin_graphql_api_id = "gid://shopify/Collection/1234567890",
            handle = "summer-collection",
            id = 1,
            title = "Summer Collection",
            published_at = "2023-06-01T12:00:00Z",
            sort_order = "manual",
            image = ImageCategory(
                src = "https://example.com/images/summer-collection.jpg",
                width = 800,
                created_at = "2023-05-01T12:00:00Z",
                height = 600
            ),
            body_html = "<p>Check out our exclusive summer collection!</p>"
        ),
        CustomCollection(
            published_scope = "global",
            updated_at = "2023-09-15T12:00:00Z",
            admin_graphql_api_id = "gid://shopify/Collection/0987654321",
            handle = "winter-collection",
            id = 2,
            title = "Winter Collection",
            published_at = "2023-11-01T12:00:00Z",
            sort_order = "manual",
            image = ImageCategory(
                src = "https://example.com/images/winter-collection.jpg",
                width = 800,
                created_at = "2023-10-01T12:00:00Z",
                height = 600
            ),
            body_html = "<p>Discover our cozy winter collection!</p>"
        ),
        CustomCollection(
            published_scope = "global",
            updated_at = "2023-08-20T12:00:00Z",
            admin_graphql_api_id = "gid://shopify/Collection/1122334455",
            handle = "autumn-collection",
            id = 3,
            title = "Autumn Collection",
            published_at = "2023-09-01T12:00:00Z",
            sort_order = "manual",
            image = ImageCategory(
                src = "https://example.com/images/autumn-collection.jpg",
                width = 800,
                created_at = "2023-08-01T12:00:00Z",
                height = 600
            ),
            body_html = "<p>Embrace the beauty of autumn with our latest collection!</p>"
        )
    )


    override suspend fun getAllProducts(): Flow<List<Product>> {
        return flow {
            productsList?.let {
                emit(it)
            } ?: emit(emptyList())
        }.catch { e ->
            emit(emptyList())
        }
    }

    override suspend fun getProductsByCollectionId(id: Long): Flow<List<Product>> {
        return flow {
            productsList?.let {
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
        TODO("Not yet implemented")
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