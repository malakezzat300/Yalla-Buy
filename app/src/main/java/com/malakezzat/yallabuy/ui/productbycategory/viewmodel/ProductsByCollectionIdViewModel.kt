package com.malakezzat.yallabuy.ui.productbycategory.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductsByCollectionIdViewModel(private val repository: ProductsRepository): ViewModel() {
    private val TAG = "ProductsByCollectionIdV"
    private val _productList = MutableStateFlow<ApiState<List<Product>>>(ApiState.Loading)
    val productList: StateFlow<ApiState<List<Product>>> get() = _productList

    init {
        getProductsByCollectionId(338050220214)
    }

    fun getProductsByCollectionId(id:Long){
        viewModelScope.launch {
        repository.getProductsByCollectionId(id)
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

                Log.i(TAG, "getProductsByCollectionId: ${productList.get(2)}")
                // Log.i(TAG, "Brands: $brands")
            }
        }
    }
}