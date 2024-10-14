package com.malakezzat.yallabuy.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
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

    private val _filterByPrice = MutableStateFlow(0f)
    val filterByPrice = _filterByPrice.asStateFlow()

    private val _draftOrderId = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val draftOrderId = _draftOrderId.asStateFlow()

    private val _draftOrders = MutableStateFlow<ApiState<DraftOrdersResponse>>(ApiState.Loading)
    val draftOrders = _draftOrders.asStateFlow()

    private val _wishListDraftOrder = MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val wishListDraftOrder = _wishListDraftOrder.asStateFlow()

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
    var filteredProducts = searchQuery
        .debounce(300) // Delay to avoid querying on every character
        .combine(_filterByPrice) { query, price ->
            Pair(query, price)
        }
        .combine(_searchProductsList) { queryAndPrice, productsState ->
            val (query, price) = queryAndPrice
            when (productsState) {
                is ApiState.Success -> {
                    if (query.isBlank()) {
                        productsState.data.filter { product ->
                            product.variants[0].price.toFloat() <= price
                        } // Filter only by price if no search query
                    } else {
                        productsState.data.filter { product ->
                            val matchQuery = product.title.contains(query, ignoreCase = true)
                            val matchPrice = product.variants[0].price.toFloat() <= price
                            matchQuery && matchPrice
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

    fun onSearchQueryChanged(query: String,price : Float) {
        _searchQuery.value = query
        _filterByPrice.value = price
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
//                        _shoppingCartDraftOrder.value = ApiState.Error("shoppingCart not found")
                    }
                    if (wishListDraftOrder.isNotEmpty()) {
                        _wishListDraftOrder.value = ApiState.Success(draftOrders.filter{
                            it.note == "wishList"
                        }[0])
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
                _wishListDraftOrder.value = ApiState.Success(DraftOrder())
                getDraftOrders()
            } catch (e: Exception) {
                Log.e("TAG", "Failed to delete draft order: ${e.message}")
            }
        }
    }

}