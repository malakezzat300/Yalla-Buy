package com.malakezzat.yallabuy.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remot.ApiState
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(var repository : ProductsRepository) : ViewModel(){
    private val _searchProductsList = MutableStateFlow<ApiState<List<Product>>>(ApiState.Loading)
    val searchProductsList = _searchProductsList.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val TAG = "searchViewModel"

    init {
        getAllProducts()
    }
    // Fetch all products
    fun getAllProducts() {
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
                    _searchProductsList.value = ApiState.Success(productList) // Set success state with data
                    Log.i(TAG, "getAllProducts: ${productList.size}")
                }
        }
    }
    val filteredProducts = searchQuery
        .debounce(300) // Delay to avoid querying on every character
        .combine(_searchProductsList) { query, productsState ->
            // Combine the search query and products list
            when (productsState) {
                is ApiState.Success -> {
                    if (query.isBlank()) {
                        productsState.data // Return all products if query is blank
                    } else {
                        // Filter products based on query
                        productsState.data.filter { product ->
                            product.title.contains(query, ignoreCase = true)
                        }
                    }
                }
                else -> emptyList() // Handle other cases such as Loading or Error
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList() // Default empty list
        )
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}