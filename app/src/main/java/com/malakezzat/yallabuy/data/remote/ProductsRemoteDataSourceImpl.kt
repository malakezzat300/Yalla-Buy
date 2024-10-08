package com.malakezzat.yallabuy.data.remote

import android.util.Log
import com.malakezzat.yallabuy.data.remot.ProductService
import com.malakezzat.yallabuy.data.remote.RetrofitHelper.apiCurrency
import com.malakezzat.yallabuy.model.Address
import com.malakezzat.yallabuy.model.AddressRequest
import com.malakezzat.yallabuy.model.AddressResponse
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.CustomerAddress
import com.malakezzat.yallabuy.model.CustomerRequest
import com.malakezzat.yallabuy.model.CustomerResponse
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Order
import com.malakezzat.yallabuy.model.Orders
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.ProductResponse
import com.malakezzat.yallabuy.model.SmartCollection
import com.malakezzat.yallabuy.model.VariantResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.http.Path

class ProductsRemoteDataSourceImpl (var productService: ProductService):
    ProductsRemoteDataSource {

    private val TAG = "ProductsRemoteDataSource"
    companion object {
        private var instance: ProductsRemoteDataSourceImpl? = null
        fun getInstance(productService: ProductService): ProductsRemoteDataSourceImpl {
            return instance ?: synchronized(this) {
                val temp = ProductsRemoteDataSourceImpl(productService)
                instance = temp
                temp
            }
        }
}

    override suspend fun getAllProducts(): Flow<List<Product>>  = flow {
        val response = productService.getAllProducts().products
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching products", e)
        emit(emptyList())
    }

    override suspend fun getProductsByCollectionId(id: Long): Flow<List<Product>> = flow  {
        val response = productService.getProductsInCollection(id).products
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching products", e)
        emit(emptyList())
    }

    override suspend fun getCategories(): Flow<List<CustomCollection>> = flow {
        val response = productService.getCategories().custom_collections
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Categories", e)
        throw e
    }

    override suspend fun getBrands(): Flow<List<SmartCollection>> = flow{
        val response = productService.getBrands().smart_collections
        emit(response)
    }.catch {e->
        Log.e(TAG, "Error fetching Brands", e)
        throw e
    }

    override suspend fun getAllOrdersForCustomerByID(id: Long): Flow<List<Order>> = flow {
        val response = productService.getAllOrdersForCustomerByID(id).orders
        emit(response)
    }.catch { e->
        Log.e(TAG, "Error fetching Brands", e)
        throw e
    }


    override suspend fun getAllDraftOrders(): Flow<DraftOrdersResponse> = flow {
        val response = productService.getAllDraftOrders()
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching AllDraftOrders", e)
        throw e
    }

    override suspend fun getDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> = flow {
        val response = productService.getDraftOrder(draftOrderId)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching DraftOrder", e)
        throw e
    }

    override suspend fun createDraftOrder(draftOrder: DraftOrderRequest): Flow<DraftOrderResponse> = flow {
        val response = productService.createDraftOrder(draftOrder)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error create DraftOrder", e)
        throw e
    }

    override suspend fun updateDraftOrder(
        draftOrderId: Long,
        draftOrder: DraftOrderRequest
    ): Flow<DraftOrderResponse> = flow {
        val response = productService.updateDraftOrder(draftOrderId,draftOrder)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error update DraftOrder", e)
        throw e
    }

    override suspend fun deleteDraftOrder(draftOrderId: Long) {
        productService.deleteDraftOrder(draftOrderId)
    }

    override suspend fun finalizeDraftOrder(draftOrderId: Long): Flow<DraftOrderResponse> = flow {
        val response = productService.finalizeDraftOrder(draftOrderId)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error finalize DraftOrder", e)
        throw e
    }
    override suspend fun getProductById(id : Long) : Flow<ProductResponse> = flow{
        val response = productService.getProductById(id)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error fetching Product by id", e)
        throw e
    }

    override suspend fun createCustomer(customerRequest: CustomerRequest) : Flow<CustomerResponse> = flow{
        val response = productService.createCustomer(customerRequest)
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error creating customer ", e)
        throw e
    }

    override suspend fun getCustomerByEmai(customerEmail: String) : Flow<CustomerSearchRespnse> = flow{
        val response = productService.getCustomerByEmail(customerEmail)
        Log.i(TAG, "getCustomerByEmai: ${response}")
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error creating customer ", e)
        throw e
    }
    override suspend fun getCustomerById(customerId: Long) : Flow<CustomerSearchRespnse> = flow{
        val response = productService.getCustomerById(customerId)
        Log.i(TAG, "getCustomerById: ${response}")
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error creating customer ", e)
        throw e
    }

    override suspend fun getVariantById(variantId: Long): Flow<VariantResponse> = flow{
        val response = productService.getVariantById(variantId)
        Log.i(TAG, "getVariantById: $response")
        emit(response)
    }.catch { e ->
        Log.e(TAG, "Error getting Variant ", e)
        throw e
    }

    override suspend fun getConversionRate(): Flow<CurrencyResponse> = flow {
        val response = apiCurrency.getLatestRates()
        emit(response)
    }.catch { e ->
        e.printStackTrace()
    }

    override suspend fun addNewAddress(customerId: Long,address: AddressRequest): Flow<CustomerAddress> = flow {
        val response = productService.addNewAddress(customerId,address)
        emit(response)
    }.catch { e ->
        e.printStackTrace()
    }

    override suspend fun getUserAddresses(customerId: Long): Flow<AddressResponse> = flow {
        val response = productService.getUserAddresses(customerId)
        emit(response)
    }.catch { e ->
        e.printStackTrace()
    }

    override suspend fun getAddressDetails(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddress> = flow {
        val response = productService.getAddressDetails(customerId,addressId)
        emit(response)
    }.catch { e ->
        e.printStackTrace()
    }

    override suspend fun updateUserAddress(
        customerId: Long,
        addressId: Long,
        address: AddressRequest
    ): Flow<CustomerAddress> = flow {
        val response = productService.updateUserAddress(customerId,addressId,address)
        emit(response)
    }.catch { e ->
        e.printStackTrace()
    }

    override suspend fun setDefaultAddress(
        customerId: Long,
        addressId: Long
    ): Flow<CustomerAddress> = flow {
        val response = productService.setDefaultAddress(customerId,addressId)
        emit(response)
    }.catch { e ->
        e.printStackTrace()
    }

    override suspend fun deleteAddress(customerId: Long, addressId: Long) {
        productService.deleteAddress(customerId,addressId)
    }
}
