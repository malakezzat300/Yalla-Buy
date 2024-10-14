package com.malakezzat.yallabuy.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.DiscountCode
import com.malakezzat.yallabuy.model.PriceRule
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Product
import com.malakezzat.yallabuy.model.SmartCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: ProductsRepository):ViewModel() {
    private val TAG = "HomeScreenViewModel"
    private val _productList = MutableStateFlow<ApiState<List<Product>>>(ApiState.Loading)
    val productList: StateFlow<ApiState<List<Product>>> get() = _productList
    private val _categoriesList = MutableStateFlow<ApiState<List<CustomCollection>>>(ApiState.Loading)
    val categoriesList: StateFlow<ApiState<List<CustomCollection>>> get() = _categoriesList
//
private val _brandsList = MutableStateFlow<ApiState<List<SmartCollection>>>(ApiState.Loading)
    val brandsList: StateFlow<ApiState<List<SmartCollection>>> get() = _brandsList

    private val _discountCodes = MutableStateFlow<List<DiscountCode>>(emptyList())
    val discountCodes: StateFlow<List<DiscountCode>> get() = _discountCodes

    private var priceRuleId: Long? = null

    private val _priceRules = MutableStateFlow<List<PriceRule>>(emptyList())
    val priceRules: StateFlow<List<PriceRule>> get() = _priceRules

    private val _customerDataByEmail = MutableStateFlow<ApiState<CustomerSearchRespnse>>(ApiState.Loading)
    val customerDataByEmail = _customerDataByEmail.asStateFlow()

    private val _conversionRate = MutableStateFlow<ApiState<CurrencyResponse?>>(ApiState.Loading)
    val conversionRate = _conversionRate.asStateFlow()

    private val _draftOrderId = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val draftOrderId = _draftOrderId.asStateFlow()

    private val _draftOrders = MutableStateFlow<ApiState<DraftOrdersResponse>>(ApiState.Loading)
    val draftOrders = _draftOrders.asStateFlow()


    private val _wishListDraftOrder = MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val wishListDraftOrder = _wishListDraftOrder.asStateFlow()

    init {
        getAllProducts()
        getAllCategories()
        fetchPriceRules()
        getBrands()
        getUserByEmail()
        getDraftOrders()
    }

    // Fetch all products
    fun getAllProducts() {
        viewModelScope.launch {
            repository.getAllProducts()
                .onStart {
                    _productList.value = ApiState.Loading // Set loading state
                }
                .catch { e ->
                    _productList.value = ApiState.Error(e.message ?: "Unknown error")
                   // _errorMessage.value = e.message // Set error message
                }
                .collect { productList ->
                    _productList.value = ApiState.Success(productList) // Set success state with data
                    //val brands = productList.map { it.vendor }.distinct()
                   // _brandsList.value = ApiState.Success(brands)

                    Log.i(TAG, "getAllProducts: ${productList.size}")
                   // Log.i(TAG, "Brands: $brands")
                }
        }
    }

    fun getAllCategories(){
        viewModelScope.launch {
            repository.getCategories()
                .onStart {
                    _categoriesList.value = ApiState.Loading
                }.catch { e->
                    _categoriesList.value = ApiState.Error(e.message?:"Unknown error")
                }.collect{categories->
                    val filteredCategories = categories.drop(1)
                    _categoriesList.value = ApiState.Success(filteredCategories)
                    Log.i(TAG, "getAllCategories: ${categories.get(0).title}")
                }
        }
    }

    fun getBrands(){
        viewModelScope.launch {
            repository.getBrands()
                .onStart {
                    _brandsList.value = ApiState.Loading
                }.catch { e->
                    _brandsList.value = ApiState.Error(e.message?:"Unknown error")
                }.collect{brands->
                    _brandsList.value = ApiState.Success(brands)
                    Log.i(TAG, "getBrands: ${brands.size}")
                }
        }
    }

    fun fetchPriceRules() {
        viewModelScope.launch {
            repository.getPriceRules().collect { rules ->
                _priceRules.value = rules.price_rules
            }
        }
    }

    fun fetchDiscountCodes(priceRuleId: Long) {
        viewModelScope.launch {
            repository.getDiscountCodes(priceRuleId).collect { discountCodes ->
                _discountCodes.value = discountCodes.discount_codes
            }
        }
    }



    fun getUserByEmail(){
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail != null) {
            repository.setUserEmail(userEmail)
        }
        viewModelScope.launch {
            if (userEmail != null) {
                repository.getCustomerByEmail(userEmail)
                    .onStart {
                        _customerDataByEmail.value = ApiState.Loading // Set loading state
                    }
                    .catch { e ->
                        _customerDataByEmail.value = ApiState.Error(e.message ?: "Unknown error")
                        // _errorMessage.value = e.message // Set error message
                        Log.i(TAG, "getCustomer: error ${e.message}")

                    }
                    .collect { customerData ->
                        _customerDataByEmail.value = ApiState.Success(customerData) // Set success state with data
                        customerData.customers.get(0).id?.let { repository.setUserId(it) }
                        Log.i(TAG, "getCustomer:  ${repository.getUserId()}")
                    }
            }
        }
    }

    fun getRate(){
        Log.i("currencyTest", "getRate: getting rate")
        viewModelScope.launch {
            repository.getConversionRate().onStart {
                _conversionRate.value = ApiState.Loading
            }
                .catch { e ->
                    _conversionRate.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { response ->
                    _conversionRate.value = ApiState.Success(response)
                }
        }
    }

    fun createDraftOrder(draftOrder: DraftOrderRequest){
        viewModelScope.launch {
            repository.createDraftOrder(draftOrder).onStart {
                _draftOrderId.value = ApiState.Loading
            }
                .catch { e ->
                    _draftOrderId.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { draftOrderResponse ->
                    val draftOrderId = draftOrderResponse.draft_order

                    if (draftOrderId != null) {
                        _draftOrderId.value = ApiState.Success(draftOrderResponse)
                    } else {
                        _draftOrderId.value = ApiState.Error("Draft Order not found")
                    }
                }
        }
    }

    fun updateDraftOrder(draftOrderId: Long,draftOrder: DraftOrderRequest){
        viewModelScope.launch {
            repository.updateDraftOrder(draftOrderId,draftOrder).onStart {
                _draftOrderId.value = ApiState.Loading
            }
                .catch { e ->
                    _draftOrderId.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { draftOrderResponse ->
                    val draftOrderId = draftOrderResponse.draft_order

                    if (draftOrderId != null) {
                        _draftOrderId.value = ApiState.Success(draftOrderResponse)
                    } else {
                        _draftOrderId.value = ApiState.Error("Draft Order not found")
                    }
                }
        }
    }


    fun getDraftOrders(){
        viewModelScope.launch {
            repository.getAllDraftOrders().onStart {
                _draftOrders.value = ApiState.Loading
            }
                .catch { e ->
                    _draftOrders.value = ApiState.Error(e.message ?: "Unknown error")
                }
                .collect { draftOrdersResponse ->
                    val draftOrders = draftOrdersResponse.draft_orders.filter {
                        it.email == FirebaseAuth.getInstance().currentUser?.email
                    }
                    val shoppingCartDraftOrder = draftOrders.filter{
                        it.note == "shoppingCart"
                    }
                    val wishListDraftOrder = draftOrders.filter{
                        it.note == "wishList"
                    }
                    if (shoppingCartDraftOrder.isNotEmpty()) {
//                        _shoppingCartDraftOrder.value = ApiState.Success(draftOrders.filter{
//                            it.note == "shoppingCart"
//                        }[0])
                    } else {
                        //_shoppingCartDraftOrder.value = ApiState.Error("shoppingCart not found")
                    }
                    if (wishListDraftOrder.isNotEmpty()) {
                        _wishListDraftOrder.value = ApiState.Success(draftOrders.filter{
                            it.note == "wishList" }[0])
                        Log.i("wishList", "getDraftOrders: ${_wishListDraftOrder.value}")
                    } else {
                        _wishListDraftOrder.value = ApiState.Error("wishList not found")
                    }
                }
        }
    }

    fun deleteDraftOrder(draftOrderId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteDraftOrder(draftOrderId)
                getDraftOrders()
            } catch (e: Exception) {
                Log.e("TAG", "Failed to delete draft order: ${e.message}")
            }
        }
    }
}