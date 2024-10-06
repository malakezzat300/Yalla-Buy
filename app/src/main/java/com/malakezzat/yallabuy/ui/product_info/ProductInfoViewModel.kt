package com.malakezzat.yallabuy.ui.product_info

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductInfoViewModel(var repository : ProductsRepository) : ViewModel() {

    private val _searchProductsList = MutableStateFlow<ApiState<Product>>(ApiState.Loading)
    val searchProductsList = _searchProductsList.asStateFlow()

    fun getProductById(id : Long) {
        viewModelScope.launch {
            repository.getAllProducts()
                .onStart {
                    _searchProductsList.value = ApiState.Loading // Set loading state
                }
                .catch { e ->
                    _searchProductsList.value = ApiState.Error(e.message ?: "Unknown error")
                    // _errorMessage.value = e.message // Set error message
                }
                .collect { productList ->
                    val product = productList.find { it.id == id }

                    if (product != null) {
                        _searchProductsList.value = ApiState.Success(product) // Set success state with the matching product
                    } else {
                        _searchProductsList.value = ApiState.Error("Product not found") // Set error state if no product matches
                    }
                }
        }
    }
}