package com.malakezzat.yallabuy.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remot.ApiState
import com.malakezzat.yallabuy.model.Category
import com.malakezzat.yallabuy.model.CustomCollection
import com.malakezzat.yallabuy.model.Product
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
private val _brandsList = MutableStateFlow<ApiState<List<String>>>(ApiState.Loading)
    val brandsList: StateFlow<ApiState<List<String>>> get() = _brandsList

    init {
        getAllProducts()
        getAllCategories()
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
                    val brands = productList.map { it.vendor }.distinct()
                    _brandsList.value = ApiState.Success(brands)

                    Log.i(TAG, "getAllProducts: ${productList.size}")
                    Log.i(TAG, "Brands: $brands")
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
                    _categoriesList.value = ApiState.Success(categories)
                    Log.i(TAG, "getAllCategories: ${categories.get(0).title}")
                }
        }
    }
}