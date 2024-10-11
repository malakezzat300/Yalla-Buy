package com.malakezzat.yallabuy.ui.productbycategory.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.malakezzat.yallabuy.data.ProductsRepository
import com.malakezzat.yallabuy.data.remote.ApiState
import com.malakezzat.yallabuy.model.CustomerSearchRespnse
import com.malakezzat.yallabuy.model.DraftOrder
import com.malakezzat.yallabuy.model.DraftOrderRequest
import com.malakezzat.yallabuy.model.DraftOrderResponse
import com.malakezzat.yallabuy.model.DraftOrdersResponse
import com.malakezzat.yallabuy.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductsByCollectionIdViewModel(private val repository: ProductsRepository): ViewModel() {
    private val TAG = "ProductsByCollectionIdV"
    private val _productList = MutableStateFlow<ApiState<List<Product>>>(ApiState.Loading)
    val productList: StateFlow<ApiState<List<Product>>> get() = _productList

    private val _draftOrderId = MutableStateFlow<ApiState<DraftOrderResponse>>(ApiState.Loading)
    val draftOrderId = _draftOrderId.asStateFlow()

    private val _draftOrders = MutableStateFlow<ApiState<DraftOrdersResponse>>(ApiState.Loading)
    val draftOrders = _draftOrders.asStateFlow()

    private val _wishListDraftOrder = MutableStateFlow<ApiState<DraftOrder>>(ApiState.Loading)
    val wishListDraftOrder = _wishListDraftOrder.asStateFlow()

    init {
        getProductsByCollectionId(338050220214)
        getDraftOrders()
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

                Log.i(TAG, "getProductsByCollectionId: ${productList.size}")
                // Log.i(TAG, "Brands: $brands")
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
}