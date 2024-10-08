package com.malakezzat.yallabuy.ui.home.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.data.remote.coupons.DiscountCode
import com.malakezzat.yallabuy.data.remote.coupons.PriceRule
import com.malakezzat.yallabuy.data.sharedpref.CurrencyPreferences
import com.malakezzat.yallabuy.model.CurrencyResponse
import com.malakezzat.yallabuy.model.CustomCollection
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

    init {
        getAllProducts()
        getAllCategories()
        fetchPriceRules()
        getBrands()
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
                _priceRules.value = rules
            }
        }
    }

    fun fetchDiscountCodes(priceRuleId: Long) {
        viewModelScope.launch {
            repository.getDiscountCodes(priceRuleId).collect { discountCodes ->
                _discountCodes.value = discountCodes
            }
        }
    }

}